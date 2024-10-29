# Login

login et mdp = mdp machine

```bash
ssh login@eluard
```

Pour acceder à oracle, on tape

```bash
. /opt/oraenv.sh
```

puis

```bash
sqlplus
```

puis username et mdp sont login

# Demarrer le projet

Pour lancer le projet

```bash
javac -cp ojdbc8.jar:. interfaces/*.java server/*.java worker/*.java client/*.java tasks/*.java database/*.java;
```

## server

```bash
java -cp ojdbc8.jar:. server.ServerApp
```

## client

```bash
java -cp ojdbc8.jar:. client.Client

```
