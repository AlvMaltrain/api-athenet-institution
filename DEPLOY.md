# Docker, CI/CD y despliegue — athenet.institution

## Cómo funciona

1. **Pull request a `main` o `develop`** → corre `build-and-test`: compila con
   Maven (`./mvnw clean verify`) y corre los tests. Si falla, no se puede
   mergear.
2. **Push a `main`** (o sea, cuando se mergea un PR a `main`) → además de
   `build-and-test`, corren dos jobs más:
   - `build-and-push-image`: arma la imagen Docker con el `Dockerfile` y la
     publica en GitHub Container Registry (GHCR), con dos tags: `latest` y
     el hash del commit (`<sha>`).
   - `deploy`: se conecta por SSH a la instancia EC2 y le dice que baje la
     imagen recién publicada y reinicie el contenedor
     (`docker compose pull app && docker compose up -d`).

Un push a `develop` solo corre `build-and-test` (no publica imagen ni
despliega) — así puedes seguir integrando cambios en `develop` sin tocar
producción, y el despliegue real ocurre recién cuando ese trabajo llega a
`main`.

## Secrets que hay que configurar en GitHub

Ve a **Settings → Secrets and variables → Actions → New repository secret**
en `github.com/AlvMaltrain/api-athenet-institution` y agrega:

| Secret        | Valor                                                            |
|---------------|-------------------------------------------------------------------|
| `EC2_HOST`    | IP pública (o DNS) de la instancia EC2                           |
| `EC2_USER`    | Usuario SSH de la instancia (`ec2-user` en Amazon Linux, `ubuntu` en Ubuntu) |
| `EC2_SSH_KEY` | Contenido **completo** del archivo `.pem` de la instancia (la clave privada, tal cual, incluyendo las líneas `-----BEGIN...-----`) |

No hace falta crear un secret para GHCR: el pipeline usa el `GITHUB_TOKEN`
que GitHub genera automáticamente en cada ejecución, tanto para publicar la
imagen como para que el EC2 pueda bajarla.

## Setup único de la instancia EC2 (esto lo tienes que hacer tú una vez, a mano)

No tengo forma de conectarme a tu EC2 desde acá, así que estos pasos los
corres tú por SSH la primera vez:

```bash
# 1. Conéctate a la instancia
ssh -i tu-clave.pem ec2-user@<IP-DE-TU-EC2>

# 2. Instala Docker (Amazon Linux 2023)
sudo dnf install -y docker
sudo systemctl enable --now docker
sudo usermod -aG docker $USER
# cierra sesión y vuelve a entrar para que el grupo tome efecto

# 3. Instala el plugin de Docker Compose
DOCKER_CONFIG=${DOCKER_CONFIG:-$HOME/.docker}
mkdir -p $DOCKER_CONFIG/cli-plugins
curl -SL https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64 \
  -o $DOCKER_CONFIG/cli-plugins/docker-compose
chmod +x $DOCKER_CONFIG/cli-plugins/docker-compose
docker compose version   # debería imprimir la versión

# 4. Crea la carpeta donde va a vivir el deploy
sudo mkdir -p /opt/athenet-institution
sudo chown $USER:$USER /opt/athenet-institution
```

Luego, desde tu máquina (no la EC2), copia el `docker-compose.yml` de este
repo a esa carpeta:

```bash
scp -i tu-clave.pem docker-compose.yml ec2-user@<IP-DE-TU-EC2>:/opt/athenet-institution/
```

Y en la EC2, crea el archivo `.env` real (usa `.env.example` como plantilla,
pero con los valores reales — este archivo **no** se sube a git):

```bash
cd /opt/athenet-institution
nano .env
```

```env
DB_NAME=institucion_db
DB_USERNAME=postgres
DB_PASSWORD=<una-clave-nueva-y-segura>
JWT_ISSUER_URI=https://login.microsoftonline.com/bfbc730b-74fb-4552-b74c-c93431273927/v2.0
IMAGE=ghcr.io/alvmaltrain/api-athenet-institution:latest
```

Por último, abre el puerto 8080 en el Security Group de la instancia
(Inbound rule: TCP 8080, origen según lo que necesites) y levanta todo por
primera vez a mano:

```bash
cd /opt/athenet-institution
docker compose pull app
docker compose up -d
```

Desde ahí en adelante, cada push a `main` va a actualizar el contenedor
automáticamente.

## ⚠️ Importante: la contraseña de la base de datos

`application.yml` tenía la contraseña de Postgres escrita en texto plano
(`simurdiera77`) y ese archivo está commiteado en el repo — o sea, quedó
expuesta en el historial de git. Ya la saqué del código (ahora se lee desde
la variable de entorno `DB_PASSWORD`, sin valor por defecto), pero el
historial de git sigue teniendo esa clave. Te recomiendo:

1. Cambiar la contraseña de tu Postgres local (y la que uses en la EC2) por
   una nueva.
2. Nunca commitear `.env` (ya quedó en `.gitignore`).

## Correr todo en local con Docker

```bash
cp .env.example .env
# edita .env con tus valores locales

docker compose up --build -d   # build local desde el Dockerfile
docker compose logs -f app     # ver logs
docker compose down            # apagar todo
```

## Correr el backend SIN Docker (como antes)

Como `DB_PASSWORD` ya no tiene un valor por defecto en `application.yml`,
tienes que exportarla antes de correr la app (o configurarla como variable
de entorno en tu IDE):

```bash
export DB_PASSWORD=simurdiera77   # o la clave que uses en tu Postgres local
./mvnw spring-boot:run
```
