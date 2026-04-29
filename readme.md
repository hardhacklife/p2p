# P2P Project

Application **Peer-to-Peer (P2P)** développée avec Spring Boot.
Chaque nœud fonctionne indépendamment avec son propre port et son propre espace de stockage local.

---

## Fonctionnement

Chaque instance lancée représente un nœud du réseau P2P.

Les données de chaque nœud sont stockées localement dans :

```text
storages/nodes/[port]
```

### Exemples

```text
storages/nodes/5000
storages/nodes/5001
storages/nodes/5002
```

* Le nœud sur `5000` écrit dans `storages/nodes/5000`
* Le nœud sur `5001` écrit dans `storages/nodes/5001`
* Le nœud sur `5002` écrit dans `storages/nodes/5002`

---

## Technologies

* Java
* Spring Boot
* Maven

---

## Structure du projet

```text
src/main/resources/
├── application.yml
├── application-node1.yml
├── application-node2.yml
└── application-node3.yml
```

---

## Configuration des nœuds

Chaque profil démarre l’application sur un port différent.

### application-node1.yml

```yaml
server:
  port: 5000
```

### application-node2.yml

```yaml
server:
  port: 5001
```

### application-node3.yml

```yaml
server:
  port: 5002
```

---

## Prérequis

Installer :

* Java 21
* Maven

Vérifier les versions :

```bash
java -version
mvn -version
```

---

## Installation

### 1. Cloner le projet

```bash
git clone https://github.com/hardhacklife/p2p.git
cd p2p
```

### 2. Compiler le projet

```bash
mvn clean install
```

---

## Lancement du projet

## Option 1 : IntelliJ IDEA

Créer 3 configurations Spring Boot avec les arguments suivants :

### Node 1

```text
--spring.profiles.active=node1
```

### Node 2

```text
--spring.profiles.active=node2
```

### Node 3

```text
--spring.profiles.active=node3
```

Puis lancer chaque configuration.

---

## Option 2 : Terminal

### Lancer Node 1

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=node1
```

### Lancer Node 2

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=node2
```

### Lancer Node 3

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=node3
```

---

## Vérification

Une fois les nœuds démarrés :

* http://localhost:5000
* http://localhost:5001
* http://localhost:5002

---

## Stockage local

Les dossiers suivants seront créés automatiquement :

```text
storages/nodes/5000
              /5001
              /5002
```

Chaque nœud garde ses propres fichiers localement.

---

## Important

* Chaque nœud doit utiliser un port différent.
* Les fichiers de configuration doivent être dans `src/main/resources/`
* Vérifier les noms :

```text
application-node1.yml
application-node2.yml
application-node3.yml
```

---

## Auteur

Projet de démonstration d’un réseau distribué P2P avec Spring Boot.
