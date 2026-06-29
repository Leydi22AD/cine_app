#!/bin/sh
# wait-for-it.sh: Espera a que un host y puerto estén disponibles.

set -e

host="$1"
shift
port="$1"
shift
cmd="$@"

until nc -z "$host" "$port"; do
  >&2 echo "MySQL no está disponible - esperando..."
  sleep 1
done

>&2 echo "MySQL está listo - ejecutando comando"
exec $cmd
