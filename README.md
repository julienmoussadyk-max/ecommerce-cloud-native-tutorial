# Architecture Cloud-Native E-Commerce

## Tutoriel Complet : De Développeur à Architecte
Un guide complet pour construire une plateforme e-commerce temps réel avec 6 microservices Spring Boot, 4 micro-frontends Angular, Kafka, Kubernetes et GCP.

## Table des Matières
1. [Introduction](#1-introduction)
2. [Spring Initializr](#2-spring-initializr)
3. [Architecture Globale](#3-architecture-globale)
4. [Structure des Projets](#4-structure-des-projets)
5. [Backend Spring Boot](#5-backend-spring-boot)
6. [Apache Kafka](#6-apache-kafka)
7. [Angular 20 + Micro-frontends](#7-angular-20--micro-frontends)
8. [Docker + Kubernetes](#8-docker--kubernetes)
9. [Observabilité](#9-observabilité)
10. [Google Cloud Platform](#10-google-cloud-platform)
11. [GitHub Actions CI/CD](#11-github-actions-cicd)
12. [Questions d'Entretien](#12-questions-dentretien)

---

## 1. Introduction
Construction d'une plateforme e-commerce temps réel avec les technologies les plus modernes de l'écosystème Java et JavaScript.

### Objectifs du Projet
Ce tutoriel vous guide dans la construction d'une plateforme e-commerce temps réel. Voici ce que vous allez construire et apprendre :
* 🚀 **6 Microservices** – Spring Boot communicant via Kafka
* 🧩 **4 Micro-frontends** – Angular interconnectés via Module Federation
* ⎈ **Kubernetes** – Infrastructure complète avec HPA (Horizontal Pod Autoscaler)
* ⚙️ **CI/CD** – Pipeline automatisé GitHub Actions ➡️ GCP
* 📊 **Observabilité** – Logs, métriques, traces distribuées

### Pourquoi cette architecture ?

| Approche Traditionnelle | Notre Architecture |
| :--- | :--- |
| 📦 Monolithe unique | ⚙️ Microservices indépendants |
| 🔗 Couplage fort | 🔄 Communication asynchrone |
| 📈 Scaling global | 🎯 Scaling ciblé par service |
| ⚠️ Déploiement risqué | 🚀 Déploiements indépendants |
| ❌ Un seul point de défaillance | 🛡️ Résilience distribuée |

> 💡 **Analogie :** Imaginez un restaurant. Un monolithe, c'est une seule personne qui prend les commandes, cuisine et sert. Notre architecture, c'est une équipe spécialisée : un serveur, un chef, un pâtissier, chacun expert dans son domaine et capable de travailler en parallèle.

### Planning sur 5 Jours

| Jour | Objectif | Chapitres |
| :--- | :--- | :--- |
| **Jour 1** | Initialisation + Architecture | 1, 2, 3, 4 |
| **Jour 2** | Backend microservices | 5 |
| **Jour 3** | Kafka + Events | 6 |
| **Jour 4** | Frontend Angular | 7 |
| **Jour 5** | Kubernetes + CI/CD + Observabilité | 8, 9, 10, 11 |

### Prérequis

* 🛠️ **Logiciels requis**
  * Java 21 (Eclipse Temurin recommandé)
  * Node.js 22 LTS
  * Docker Desktop
  * IntelliJ IDEA Ultimate
  * Git
* 🧠 **Connaissances supposées**
  * Bases de Java et Spring
  * Notions de HTML/CSS/TypeScript
  * Utilisation basique de Git
  * Ligne de commande basique
