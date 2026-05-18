# Architecture Cloud-Native E-Commerce

> **Tutoriel Complet : De Développeur à Architecte**

Un guide complet pour construire une plateforme e-commerce temps réel avec 6 microservices Spring Boot, 4 micro-frontends Angular, Kafka, Kubernetes et GCP.

---

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


# Introduction

> Construction d'une plateforme e-commerce temps réel avec les technologies les plus modernes de l'écosystème Java et JavaScript.


## Objectifs du Projet

Ce tutoriel vous guide dans la construction d'une plateforme e-commerce temps réel. Voici ce que vous allez construire et apprendre :

- **⚙️ 6 Microservices** — Spring Boot communicant via Kafka
- **🖥️ 4 Micro-frontends** — Angular interconnectés via Module Federation
- **☸️ Kubernetes** — Infrastructure complète avec HPA
- **🚀 CI/CD** — Pipeline automatisé GitHub Actions → GCP
- **📊 Observabilité** — Logs, métriques, traces distribuées

### Pourquoi cette architecture ?

| Approche Traditionnelle | Notre Architecture |
| --- | --- |
| Monolithe unique | ✅ Microservices indépendants |
| Couplage fort | ✅ Communication asynchrone |
| Scaling global | ✅ Scaling ciblé par service |
| Déploiement risqué | ✅ Déploiements indépendants |
| Un seul point de défaillance | ✅ Résilience distribuée |

> Analogie : Imaginez un restaurant. Un monolithe, c'est une seule personne qui prend les commandes, cuisine et sert. Notre architecture, c'est une équipe spécialisée : un serveur, un chef, un pâtissier, chacun expert dans son domaine et capable de travailler en parallèle.

## Planning sur 5 Jours

| Jour | Objectif | Chapitres |
| --- | --- | --- |
| Jour 1 | Initialisation + Architecture | 1, 2, 3, 4 |
| Jour 2 | Backend microservices | 5 |
| Jour 3 | Kafka + Events | 6 |
| Jour 4 | Frontend Angular | 7 |
| Jour 5 | Kubernetes + CI/CD + Observabilité | 8, 9, 10, 11 |

## Prérequis

**🛠️ Logiciels requis**
- Java 21 (Eclipse Temurin recommandé)
- Node.js 22 LTS
- Docker Desktop
- IntelliJ IDEA Ultimate
- Git

**📚 Connaissances supposées**
- Bases de Java et Spring
- Notions de HTML/CSS/TypeScript
- Utilisation basique de Git
- Ligne de commande basique


---


# Spring Initializr

> Générateur de projets Spring Boot sur **start.spring.io** — votre point de départ pour chaque microservice.


Spring Initializr crée automatiquement la structure de base d'un projet avec toutes les dépendances configurées.

> Analogie : C'est comme un architecte qui dessine les plans de votre maison. Vous décrivez vos besoins (nombre de pièces, style), et il produit des plans conformes aux normes de construction.

## Configuration d'un Microservice

Pour créer notre order-service , voici les paramètres :

| Paramètre | Valeur | Explication |
| --- | --- | --- |
| Project | Maven | Gestionnaire de dépendances le plus répandu |
| Language | Java | Notre langage backend |
| Spring Boot | 4.0.x | Dernière version stable |
| Group | com.ecommerce | Identifiant de votre organisation |
| Artifact | order-service | Nom du microservice |
| Packaging | Jar | Format d'exécution standard |
| Java | 21 | Version LTS avec virtual threads |

### Dépendances à sélectionner

> ✓ Spring Web    ✓ Spring Data JPA    ✓ PostgreSQL Driver
> ✓ Spring for Apache Kafka    ✓ Spring Boot Actuator    ✓ Spring Security
> ✓ Validation    ✓ Lombok

## Structure du Projet Généré

**`order-service/`**

```
order-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ecommerce/orderservice/
│   │   │       └── OrderServiceApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/
│           └── com/ecommerce/orderservice/
│               └── OrderServiceApplicationTests.java
├── pom.xml
├── mvnw
├── mvnw.cmd
└── .gitignore
```

## Comprendre le pom.xml

Le pom.xml est le fichier de configuration Maven. Il définit les dépendances, plugins et propriétés du projet.

**`pom.xml`**

```xml
<!-- pom.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    
    <!-- Héritage Spring Boot : fournit les versions par défaut -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>4.0.0</version>
    </parent>
    
    <!-- Identité du projet -->
    <groupId>com.ecommerce</groupId>
    <artifactId>order-service</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    
    <properties>
        <java.version>21</java.version>
    </properties>
    
    <dependencies>
        <!-- Chaque starter inclut plusieurs bibliothèques -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- Autres dépendances... -->
    </dependencies>
</project>
```

> Maven télécharge les dépendances depuis Maven Central et les stocke localement dans ~/.m2/repository . Spring Boot Parent garantit que toutes les versions sont compatibles entre elles.

## Configuration application.yml

Renommez application.properties en application.yml pour une meilleure lisibilité :

**`src/main/resources/application.yml`**

```yaml
spring:
  application:
    name: order-service
  
  datasource:
    url: jdbc:postgresql://localhost:5432/ordersdb
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  
  kafka:
    bootstrap-servers: localhost:9092

server:
  port: 8081

management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus
```

| Propriété | Rôle | Impact Production |
| --- | --- | --- |
| ddl-auto: validate | Vérifie le schéma sans le modifier | Évite les modifications accidentelles |
| show-sql: false | Désactive les logs SQL | Réduit les I/O disque |
| ${DB_PASSWORD:postgres} | Variable d'environnement avec défaut | Sécurise les secrets |


---


# Architecture Globale

> Vue d'ensemble de la plateforme : micro-frontends, API Gateway, microservices, Kafka et bases de données.


## Vue d'Ensemble

```
┌─────────── Frontend — Micro-frontends ────────────┐
│  Shell App    Catalog MFE   Checkout MFE  Admin MFE│
└──────────────────────┬────────────────────────────┘
                       ↓
            ┌─────── API Gateway ────────┐
            │  Spring Cloud Gateway      │
            └──┬──────┬──────┬──────────┘
               ↓      ↓      ↓
    ┌──────────────────────────────────────┐
    │         Backend — Microservices       │
    │  Auth  Order  Payment  Inventory  Notif│
    └──────────────┬───────────────────────┘
                   ↓
          ┌──── Apache Kafka ────┐
          └──────────────────────┘
               ↓          ↓
        PostgreSQL      MongoDB
```

### Responsabilités des Services

| Service | Responsabilité | Base de données | Events produits |
| --- | --- | --- | --- |
| auth-service | Authentification, JWT | PostgreSQL | UserRegistered |
| order-service | Gestion commandes | PostgreSQL | OrderCreated, OrderUpdated |
| payment-service | Transactions | PostgreSQL | PaymentCompleted, PaymentFailed |
| inventory-service | Stock produits | MongoDB | InventoryReserved, StockUpdated |
| notification-service | Emails, push | — | — (consommateur uniquement) |
| gateway-service | Routage, auth | — | — |

## Flux de Données : Commande Client

```
Client → Gateway → Order Service
                       ↓
               OrderCreatedEvent → Kafka
                       ↓
               ← 201 Created (immédiat)

Kafka → Payment Service → PaymentCompletedEvent → Kafka
Kafka → Inventory Service → InventoryReservedEvent → Kafka
Kafka → Notification Service → Email confirmation → Client
```

> Pourquoi asynchrone ? Le client n'attend pas la fin du paiement. Il reçoit immédiatement une confirmation que sa commande est enregistrée. Le traitement continue en arrière-plan, et il sera notifié par email.

## Patterns Architecturaux Utilisés

- **🔀 API Gateway Pattern** — Point d'entrée unique · Centralise l'authentification · Gère le rate limiting
- **📡 Event-Driven Architecture** — Services découplés · Communication asynchrone · Eventual consistency
- **🗄️ Database per Service** — Chaque service possède sa base · Pas de partage de schéma · Autonomie complète
- **⚡ CQRS (simplifié)** — Séparation lecture/écriture · Optimisation des performances


---


# Structure des Projets

> Organisation des répertoires et règles de dépendances entre les couches.


## Organisation des Répertoires

**`ecommerce-platform/`**

```
ecommerce-platform/
├── backend/
│   ├── auth-service/
│   ├── order-service/
│   ├── payment-service/
│   ├── inventory-service/
│   ├── notification-service/
│   ├── gateway-service/
│   └── shared-lib/
├── frontend/
│   ├── shell/
│   ├── catalog-mfe/
│   ├── checkout-mfe/
│   └── admin-mfe/
├── infrastructure/
│   ├── kubernetes/
│   ├── docker/
│   └── terraform/
├── .github/
│   └── workflows/
└── docs/
```

## Structure Interne d'un Microservice

**`order-service/`**

```
order-service/
├── src/main/java/com/ecommerce/orderservice/
│   ├── OrderServiceApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   ├── KafkaConfig.java
│   │   └── OpenApiConfig.java
│   ├── controller/
│   │   └── OrderController.java
│   ├── service/
│   │   ├── OrderService.java
│   │   └── impl/
│   │       └── OrderServiceImpl.java
│   ├── repository/
│   │   └── OrderRepository.java
│   ├── domain/
│   │   ├── entity/
│   │   │   └── Order.java
│   │   ├── event/
│   │   │   └── OrderCreatedEvent.java
│   │   └── dto/
│   │       ├── CreateOrderRequest.java
│   │       └── OrderResponse.java
│   ├── messaging/
│   │   ├── producer/
│   │   │   └── OrderEventProducer.java
│   │   └── consumer/
│   │       └── PaymentEventConsumer.java
│   └── exception/
│       ├── OrderNotFoundException.java
│       └── GlobalExceptionHandler.java
└── src/main/resources/
    ├── application.yml
    └── db/migration/
        └── V1__create_orders_table.sql
```

## Règles de Dépendances

> Règle d'or : Les dépendances vont toujours vers l'intérieur (Domain). Jamais vers l'extérieur.

| Couche | Peut dépendre de | Ne peut PAS dépendre de |
| --- | --- | --- |
| Controller | Service, DTO | Repository, Entity |
| Service | Repository, Domain, Messaging | Controller |
| Repository | Entity | Service, Controller |
| Domain | Rien (ou shared-lib) | Tout le reste |


---


# Backend Spring Boot

> Implémentation complète des microservices : Auth Service, Order Service, et Shared Library.


## Auth Service

### 5.1.1 Rôle et Responsabilités

L' auth-service gère l'authentification et l'autorisation de toute la plateforme. Il délivre des tokens JWT que les autres services valident.

### 5.1.2 Entité User

**`com/ecommerce/authservice/domain/entity/User.java`**

```java
// com/ecommerce/authservice/domain/entity/User.java
package com.ecommerce.authservice.domain.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String passwordHash;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    
    @Column(nullable = false)
    private Instant createdAt;
    
    private Instant lastLoginAt;
    
    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }
    
    // Getters et setters...
}

enum Role { USER, ADMIN }
```

| Annotation | Rôle | Impact |
| --- | --- | --- |
| @Entity | Marque la classe comme persistante | Hibernate crée la table |
| @Table(name = "users") | Nom explicite de la table | Évite les conflits avec mots réservés |
| @GeneratedValue(IDENTITY) | Auto-increment PostgreSQL | Délègue la génération à la DB |
| @ElementCollection | Collection de valeurs simples | Table jointe automatique |
| @PrePersist | Hook avant insertion | Initialise createdAt automatiquement |

### 5.1.3 Service JWT

**`com/ecommerce/authservice/service/JwtService.java`**

```java
// com/ecommerce/authservice/service/JwtService.java
package com.ecommerce.authservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

@Service
public class JwtService {
    
    private final SecretKey key;
    private final long expirationMinutes;
    
    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-minutes:60}") long expirationMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMinutes = expirationMinutes;
    }
    
    public String generateToken(String email, Set<String> roles) {
        Instant now = Instant.now();
        
        return Jwts.builder()
                .subject(email)
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }
    
    public Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
```

> Sécurité : Le secret JWT doit faire au minimum 256 bits (32 caractères). En production, stockez-le dans un gestionnaire de secrets (Vault, GCP Secret Manager).

### 5.1.4 Configuration Sécurité

**`com/ecommerce/authservice/config/SecurityConfig.java`**

```java
// com/ecommerce/authservice/config/SecurityConfig.java
package com.ecommerce.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // API stateless
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated())
            .build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Cost factor 12
    }
}
```

| Choix | Raison |
| --- | --- |
| CSRF désactivé | API REST stateless, pas de cookies |
| Sessions STATELESS | Scalabilité horizontale |
| BCrypt cost 12 | Compromis sécurité/performance |

## Order Service

### 5.2.1 Entité Order

**`com/ecommerce/orderservice/domain/entity/Order.java`**

```java
// com/ecommerce/orderservice/domain/entity/Order.java
package com.ecommerce.orderservice.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String userId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(nullable = false)
    private Instant createdAt;
    
    @Version
    private Long version; // Optimistic locking
    
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        recalculateTotal();
    }
    
    private void recalculateTotal() {
        this.totalAmount = items.stream()
            .map(OrderItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

enum OrderStatus { PENDING, PAID, SHIPPED, DELIVERED, CANCELLED }
```

> •
> @GeneratedValue(UUID)
> : Identifiants uniques sans coordination centralisée
> •
> @Version
> : Verrouillage optimiste pour éviter les modifications concurrentes
> •
> BigDecimal
> : Précision monétaire (jamais de double pour l'argent !)

### 5.2.2 Controller REST

**`com/ecommerce/orderservice/controller/OrderController.java`**

```java
// com/ecommerce/orderservice/controller/OrderController.java
package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.domain.dto.*;
import com.ecommerce.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @RequestHeader("X-User-Id") String userId) {
        
        OrderResponse order = orderService.createOrder(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }
    
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getUserOrders(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        return ResponseEntity.ok(orderService.getUserOrders(userId, page, size));
    }
}
```

### 5.2.3 Service avec Publication d'Événements

**`com/ecommerce/orderservice/service/impl/OrderServiceImpl.java`**

```java
// com/ecommerce/orderservice/service/impl/OrderServiceImpl.java
package com.ecommerce.orderservice.service.impl;

import com.ecommerce.orderservice.domain.entity.Order;
import com.ecommerce.orderservice.domain.event.OrderCreatedEvent;
import com.ecommerce.orderservice.messaging.producer.OrderEventProducer;
import com.ecommerce.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Service
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderEventProducer eventProducer;
    
    public OrderServiceImpl(OrderRepository orderRepository, OrderEventProducer eventProducer) {
        this.orderRepository = orderRepository;
        this.eventProducer = eventProducer;
    }
    
    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request, String userId) {
        // 1. Créer l'entité
        Order order = new Order();
        order.setUserId(userId);
        order.setCreatedAt(Instant.now());
        
        request.items().forEach(item -> 
            order.addItem(new OrderItem(item.productId(), item.quantity(), item.price()))
        );
        
        // 2. Persister en base
        Order saved = orderRepository.save(order);
        
        // 3. Publier l'événement
        eventProducer.sendOrderCreated(new OrderCreatedEvent(
            saved.getId(),
            saved.getUserId(),
            saved.getTotalAmount(),
            saved.getItems().stream().map(this::toItemEvent).toList()
        ));
        
        return toResponse(saved);
    }
}
```

> Attention Production : L'événement est publié après la transaction. Si Kafka est indisponible, la commande existe en base mais l'événement n'est pas envoyé. Pour garantir la cohérence, utilisez le pattern Transactional Outbox (événements stockés en base puis publiés par un processus séparé).

## Shared Library

Pour éviter la duplication des DTOs d'événements entre services :

**`shared-lib : com/ecommerce/shared/event/OrderCreatedEvent.java`**

```java
// shared-lib : com/ecommerce/shared/event/OrderCreatedEvent.java
package com.ecommerce.shared.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderCreatedEvent(
    String eventId,
    String orderId,
    String userId,
    BigDecimal totalAmount,
    List<OrderItemEvent> items,
    Instant occurredAt
) {
    public OrderCreatedEvent {
        if (eventId == null) eventId = java.util.UUID.randomUUID().toString();
        if (occurredAt == null) occurredAt = Instant.now();
    }
}
```

> Bonnes pratiques Events : Utilisez des records Java (immuables), incluez toujours un eventId unique et un timestamp occurredAt .


---


# Apache Kafka

> Plateforme de streaming d'événements distribuée — le cœur de la communication asynchrone entre services.


## Concepts Fondamentaux

> Analogie : Kafka est comme un journal de bord de navire. Chaque événement est écrit dans l'ordre, conservé pendant une durée définie, et n'importe qui peut lire l'historique depuis n'importe quel point.

| Concept | Définition | Analogie |
| --- | --- | --- |
| Topic | Catégorie de messages | Chaîne TV thématique |
| Partition | Sous-division ordonnée d'un topic | Plusieurs files parallèles |
| Offset | Position d'un message dans une partition | Numéro de page |
| Consumer Group | Ensemble de consommateurs coordonnés | Équipe de lecteurs |
| Replica | Copie d'une partition sur un autre broker | Sauvegarde |

## Configuration Docker Compose

**`infrastructure/docker/docker-compose.yml`**

```yaml
services:
  kafka:
    image: apache/kafka:3.8.0
    hostname: kafka
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_NODE_ID: 1
      KAFKA_PROCESS_ROLES: broker,controller
      KAFKA_LISTENERS: PLAINTEXT://:9092,CONTROLLER://:9093
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_CONTROLLER_QUORUM_VOTERS: 1@kafka:9093
      KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
      KAFKA_LOG_DIRS: /var/lib/kafka/data
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "false"
      KAFKA_NUM_PARTITIONS: 3
      KAFKA_DEFAULT_REPLICATION_FACTOR: 1
    volumes:
      - kafka_data:/var/lib/kafka/data

  kafka-ui:
    image: provectuslabs/kafka-ui:latest
    ports:
      - "8080:8080"
    environment:
      KAFKA_CLUSTERS_0_NAME: local
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:9092
    depends_on:
      - kafka

volumes:
  kafka_data:
```

> Cette configuration utilise KRaft (Kafka sans ZooKeeper), le mode recommandé depuis Kafka 3.3.

## Configuration Spring Kafka

**`order-service/src/main/resources/application.yml`**

```yaml
spring:
  kafka:
    bootstrap-servers: ${KAFKA_SERVERS:localhost:9092}
    
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      acks: all  # Attendre confirmation de toutes les replicas
      retries: 3
      properties:
        enable.idempotence: true  # Évite les doublons
    
    consumer:
      group-id: ${spring.application.name}
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: com.ecommerce.shared.event
```

| Propriété | Valeur | Explication |
| --- | --- | --- |
| acks: all | Confirmation complète | Durabilité maximale |
| enable.idempotence: true | Déduplication | Évite doublons en cas de retry |
| auto-offset-reset: earliest | Depuis le début | Rattrape les messages manqués |

## Producer

**`com/ecommerce/orderservice/messaging/producer/OrderEventProducer.java`**

```java
// com/ecommerce/orderservice/messaging/producer/OrderEventProducer.java
package com.ecommerce.orderservice.messaging.producer;

import com.ecommerce.shared.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Component
public class OrderEventProducer {
    
    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);
    private static final String TOPIC = "orders";
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public OrderEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void sendOrderCreated(OrderCreatedEvent event) {
        // La clé détermine la partition (même clé = même partition = ordre garanti)
        String key = event.userId();
        
        CompletableFuture<SendResult<String, Object>> future = 
            kafkaTemplate.send(TOPIC, key, event);
        
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Échec envoi event {} : {}", event.eventId(), ex.getMessage());
            } else {
                log.info("Event {} envoyé sur partition {}, offset {}", 
                    event.eventId(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
            }
        });
    }
}
```

> Clé de partitionnement : En utilisant userId comme clé, toutes les commandes d'un même utilisateur vont dans la même partition, garantissant leur ordre de traitement.

## Consumer avec Retry et DLT

**`com/ecommerce/paymentservice/messaging/consumer/OrderEventConsumer.java`**

```java
// com/ecommerce/paymentservice/messaging/consumer/OrderEventConsumer.java
package com.ecommerce.paymentservice.messaging.consumer;

import com.ecommerce.shared.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {
    
    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);
    
    private final PaymentService paymentService;
    
    public OrderEventConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    @RetryableTopic(
        attempts = "3",
        backoff = @Backoff(delay = 1000, multiplier = 2),
        dltStrategy = DltStrategy.FAIL_ON_ERROR,
        dltTopicSuffix = "-dlt"
    )
    @KafkaListener(topics = "orders", groupId = "payment-service")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Reçu OrderCreatedEvent: {}", event.orderId());
        
        // Le traitement peut échouer et sera automatiquement retenté
        paymentService.processPayment(event);
    }
    
    @DltHandler
    public void handleDlt(OrderCreatedEvent event, Exception ex) {
        log.error("Message en DLT après échecs: orderId={}, error={}", 
            event.orderId(), ex.getMessage());
        // Alerter l'équipe, stocker pour analyse manuelle
    }
}
```

> Stratégie de Retry :
> Message reçu → Retry 1 (délai 1s) → Retry 2 (délai 2s) → Retry 3 (délai 4s) → Dead Letter Topic

## Monitoring Kafka

| Métrique | Seuil d'alerte | Signification |
| --- | --- | --- |
| Consumer lag | > 1000 | Consommateurs dépassés |
| Under-replicated partitions | > 0 | Risque de perte de données |
| Request latency p99 | > 100ms | Performance dégradée |
| Messages in DLT | > 0 | Erreurs de traitement |


---


# Angular 20 + Micro-frontends

> Architecture micro-frontend avec Module Federation — chaque équipe possède une partie de l'interface de bout en bout.


## Architecture Micro-frontend

```
┌─────────────── Navigateur ─────────────────────────┐
│                                                     │
│  Shell Application (Port 4200)                      │
│  ┌────────────────────────────────────────────┐    │
│  │        Chargés dynamiquement               │    │
│  │  Catalog MFE  Checkout MFE  Admin MFE      │    │
│  │  (Port 4201)  (Port 4202)   (Port 4203)    │    │
│  └────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────┘
```

- **🚀 Déploiements indépendants** — Chaque MFE se déploie sans toucher les autres
- **👥 Équipes autonomes** — Ownership complet front + back par équipe
- **🔒 Isolation des pannes** — Un MFE en erreur n'impacte pas les autres

## Configuration Module Federation

### Shell Application (hôte)

**`frontend/shell/webpack.config.js`**

```typescript
// frontend/shell/webpack.config.js
const { ModuleFederationPlugin } = require('@angular-architects/module-federation/webpack');

module.exports = {
  output: {
    uniqueName: "shell",
    publicPath: "auto"
  },
  plugins: [
    new ModuleFederationPlugin({
      name: "shell",
      remotes: {
        "catalog": "catalog@http://localhost:4201/remoteEntry.js",
        "checkout": "checkout@http://localhost:4202/remoteEntry.js",
        "admin": "admin@http://localhost:4203/remoteEntry.js"
      },
      shared: {
        "@angular/core": { singleton: true, strictVersion: true },
        "@angular/common": { singleton: true, strictVersion: true },
        "@angular/router": { singleton: true, strictVersion: true },
        "rxjs": { singleton: true, strictVersion: true }
      }
    })
  ]
};
```

### Catalog MFE (remote)

**`frontend/catalog-mfe/webpack.config.js`**

```typescript
// frontend/catalog-mfe/webpack.config.js
const { ModuleFederationPlugin } = require('@angular-architects/module-federation/webpack');

module.exports = {
  output: {
    uniqueName: "catalog",
    publicPath: "auto"
  },
  plugins: [
    new ModuleFederationPlugin({
      name: "catalog",
      filename: "remoteEntry.js",
      exposes: {
        './CatalogModule': './src/app/catalog/catalog.module.ts'
      },
      shared: {
        "@angular/core": { singleton: true, strictVersion: true },
        "@angular/common": { singleton: true, strictVersion: true }
      }
    })
  ]
};
```

## Routing Dynamique

**`frontend/shell/src/app/app.routes.ts`**

```typescript
// frontend/shell/src/app/app.routes.ts
import { Routes } from '@angular/router';
import { loadRemoteModule } from '@angular-architects/module-federation';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./home/home.module').then(m => m.HomeModule)
  },
  {
    path: 'catalog',
    loadChildren: () => loadRemoteModule({
      type: 'module',
      remoteEntry: 'http://localhost:4201/remoteEntry.js',
      exposedModule: './CatalogModule'
    }).then(m => m.CatalogModule)
  },
  {
    path: 'checkout',
    loadChildren: () => loadRemoteModule({
      type: 'module',
      remoteEntry: 'http://localhost:4202/remoteEntry.js',
      exposedModule: './CheckoutModule'
    }).then(m => m.CheckoutModule)
  },
  {
    path: 'admin',
    loadChildren: () => loadRemoteModule({
      type: 'module',
      remoteEntry: 'http://localhost:4203/remoteEntry.js',
      exposedModule: './AdminModule'
    }).then(m => m.AdminModule),
    canActivate: [AdminGuard]
  }
];
```

## Communication entre MFEs

### Service partagé via CustomEvent

**`shared/event-bus.service.ts`**

```typescript
// shared/event-bus.service.ts
export interface MfeEvent<T = unknown> {
  type: string;
  payload: T;
  source: string;
}

export class EventBus {
  static emit<T>(event: MfeEvent<T>): void {
    window.dispatchEvent(new CustomEvent('mfe-event', { detail: event }));
  }

  static on<T>(type: string, callback: (payload: T) => void): () => void {
    const handler = (e: CustomEvent<MfeEvent<T>>) => {
      if (e.detail.type === type) {
        callback(e.detail.payload);
      }
    };
    window.addEventListener('mfe-event', handler as EventListener);
    return () => window.removeEventListener('mfe-event', handler as EventListener);
  }
}

// Utilisation dans Catalog MFE
EventBus.emit({
  type: 'ADD_TO_CART',
  payload: { productId: '123', quantity: 1 },
  source: 'catalog'
});

// Utilisation dans Shell
EventBus.on<CartItem>('ADD_TO_CART', (item) => {
  this.cartService.addItem(item);
});
```

## Composant Produit

**`frontend/catalog-mfe/src/app/catalog/product-card.component.ts`**

```typescript
// frontend/catalog-mfe/src/app/catalog/product-card.component.ts
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Product } from '../models/product.model';

@Component({
  selector: 'app-product-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <article class="product-card">
      <img [src]="product.imageUrl" [alt]="product.name" loading="lazy">
      <div class="product-info">
        <h3>{{ product.name }}</h3>
        <p class="price">{{ product.price | currency:'EUR' }}</p>
        <p class="stock" [class.low]="product.stock < 10">
          {{ product.stock }} en stock
        </p>
        <button 
          (click)="addToCart()" 
          [disabled]="product.stock === 0">
          Ajouter au panier
        </button>
      </div>
    </article>
  `,
  styles: [`
    .product-card {
      border: 1px solid #e0e0e0;
      border-radius: 8px;
      overflow: hidden;
      transition: box-shadow 0.2s;
    }
    .product-card:hover {
      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    }
    .stock.low { color: #f44336; }
  `]
})
export class ProductCardComponent {
  @Input({ required: true }) product!: Product;
  @Output() addedToCart = new EventEmitter<Product>();

  addToCart(): void {
    this.addedToCart.emit(this.product);
    EventBus.emit({
      type: 'ADD_TO_CART',
      payload: { productId: this.product.id, quantity: 1 },
      source: 'catalog'
    });
  }
}
```


---


# Docker + Kubernetes

> Conteneurisation et orchestration de la plateforme — du Dockerfile au cluster Kubernetes avec autoscaling.


## Concepts Docker

> Dockerfile
> = Recette pour construire une image
> Image
> = Template immuable
> Container
> = Instance d'exécution d'une image

### Dockerfile Optimisé (Multi-stage)

**`backend/order-service/Dockerfile`**

```dockerfile
# Étape 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN ./mvnw package -DskipTests

# Étape 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Sécurité: utilisateur non-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Copier uniquement le JAR
COPY --from=builder /app/target/*.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

| Approche | Taille image | Sécurité |
| --- | --- | --- |
| Image unique avec JDK | ~500 MB | Outils de build exposés |
| Multi-stage JRE | ~200 MB | Surface d'attaque réduite |

## Concepts Kubernetes

| Concept | Rôle | Analogie |
| --- | --- | --- |
| Pod | Plus petite unité déployable | Container(s) cohabitants |
| Deployment | Gère les replicas de pods | Chef d'équipe |
| Service | Expose les pods, load balancing | Standard téléphonique |
| Ingress | Routage HTTP externe | Réceptionniste |
| ConfigMap | Configuration non sensible | Fichier .env |
| Secret | Données sensibles (chiffrées) | Coffre-fort |

## Manifestes Kubernetes

### Deployment

**`infrastructure/kubernetes/order-service/deployment.yaml`**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: order-service
  labels:
    app: order-service
spec:
  replicas: 2
  selector:
    matchLabels:
      app: order-service
  template:
    metadata:
      labels:
        app: order-service
    spec:
      containers:
        - name: order-service
          image: gcr.io/my-project/order-service:latest
          ports:
            - containerPort: 8080
          
          # Ressources garanties et limites
          resources:
            requests:
              memory: "512Mi"
              cpu: "250m"
            limits:
              memory: "1Gi"
              cpu: "1000m"
          
          # Probes de santé
          livenessProbe:
            httpGet:
              path: /actuator/health/liveness
              port: 8080
            initialDelaySeconds: 30
            periodSeconds: 10
          
          readinessProbe:
            httpGet:
              path: /actuator/health/readiness
              port: 8080
            initialDelaySeconds: 10
            periodSeconds: 5
          
          # Variables d'environnement
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: "kubernetes"
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: db-credentials
                  key: password
          
          envFrom:
            - configMapRef:
                name: order-service-config
```

### Service et Ingress

**`infrastructure/kubernetes/order-service/service.yaml`**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: order-service
spec:
  selector:
    app: order-service
  ports:
    - port: 80
      targetPort: 8080
  type: ClusterIP
```

**`infrastructure/kubernetes/ingress.yaml`**

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: api-ingress
  annotations:
    kubernetes.io/ingress.class: nginx
    cert-manager.io/cluster-issuer: letsencrypt-prod
spec:
  tls:
    - hosts:
        - api.ecommerce.com
      secretName: api-tls
  rules:
    - host: api.ecommerce.com
      http:
        paths:
          - path: /api/orders
            pathType: Prefix
            backend:
              service:
                name: order-service
                port:
                  number: 80
          - path: /api/payments
            pathType: Prefix
            backend:
              service:
                name: payment-service
                port:
                  number: 80
```

### Horizontal Pod Autoscaler

**`infrastructure/kubernetes/order-service/hpa.yaml`**

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: order-service-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: order-service
  minReplicas: 2
  maxReplicas: 10
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
    - type: Resource
      resource:
        name: memory
        target:
          type: Utilization
          averageUtilization: 80
```

> Production : Le HPA ajuste automatiquement le nombre de pods. À 70% CPU moyen, il ajoute des pods. En dessous de 50%, il en retire (jusqu'au minimum de 2).


---


# Observabilité

> Les trois piliers : Logs, Métriques, Traces distribuées — pour comprendre ce qui se passe en production.


- **📋 Logs** — Que s'est-il passé ? → ELK Stack Ex: "Erreur de paiement pour order-123"
- **📊 Métriques** — Combien/combien de temps ? → Prometheus Ex: "P99 latence = 150ms"
- **🔍 Traces** — Quel chemin ? → Jaeger Ex: "Gateway → Order → Kafka → Payment"

## Configuration OpenTelemetry

**`src/main/resources/application.yml`**

```yaml
management:
  tracing:
    sampling:
      probability: 1.0  # 100% en dev, réduire en prod
  
  otlp:
    tracing:
      endpoint: http://otel-collector:4317
    metrics:
      export:
        enabled: true

  endpoints:
    web:
      exposure:
        include: health,info,prometheus,metrics

  metrics:
    tags:
      application: ${spring.application.name}
```

### Spans personnalisés avec @Observed

**`PaymentService.java`**

```java
// PaymentService.java
// Ajout automatique de spans personnalisés
@Service
public class PaymentService {
    
    private final Tracer tracer;
    
    public PaymentService(Tracer tracer) {
        this.tracer = tracer;
    }
    
    @Observed(name = "payment.process")
    public PaymentResult processPayment(Order order) {
        Span span = tracer.currentSpan();
        span.tag("orderId", order.getId());
        span.tag("amount", order.getTotalAmount().toString());
        
        // Traitement...
        return doProcessPayment(order);
    }
}
```

## Métriques Prometheus Personnalisées

**`MetricsConfig.java`**

```java
// MetricsConfig.java
@Configuration
public class MetricsConfig {
    
    @Bean
    public MeterBinder orderMetrics() {
        return registry -> {
            Counter.builder("orders.created.total")
                .description("Total des commandes créées")
                .register(registry);
            
            Timer.builder("orders.processing.duration")
                .description("Durée de traitement des commandes")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);
        };
    }
}
```

## Dashboard Grafana — Requêtes PromQL

| Panneau | Requête PromQL | Seuil alerte |
| --- | --- | --- |
| Requêtes/sec | rate(http_server_requests_total[5m]) | — |
| Latence P99 | histogram_quantile(0.99, rate(http_server_requests_bucket[5m])) | > 500ms |
| Taux d'erreur | rate(http_server_requests_total{status=~"5.."}[5m]) / rate(...[5m]) | > 1% |
| JVM Heap | jvm_memory_used_bytes{area="heap"} | > 80% |

## Logs Structurés (JSON)

**`src/main/resources/logback-spring.xml`**

```xml
<!-- src/main/resources/logback-spring.xml -->
<configuration>
    <springProfile name="kubernetes">
        <appender name="JSON" class="ch.qos.logback.core.ConsoleAppender">
            <encoder class="net.logstash.logback.encoder.LogstashEncoder">
                <includeMdcKeyName>traceId</includeMdcKeyName>
                <includeMdcKeyName>spanId</includeMdcKeyName>
                <includeMdcKeyName>userId</includeMdcKeyName>
            </encoder>
        </appender>
        <root level="INFO">
            <appender-ref ref="JSON"/>
        </root>
    </springProfile>
</configuration>
```

**`Exemple de log structuré`**

```json
{
  "@timestamp": "2026-05-17T10:30:00.000Z",
  "level": "INFO",
  "logger": "c.e.orderservice.service.OrderServiceImpl",
  "message": "Commande créée",
  "traceId": "abc123",
  "spanId": "def456",
  "orderId": "order-789",
  "userId": "user-001",
  "amount": 149.99
}
```


---


# Google Cloud Platform

> Déploiement sur GKE, Cloud SQL, Artifact Registry et Secret Manager.


## Création du Cluster GKE

**`Terminal`**

```bash
gcloud container clusters create ecommerce-cluster \
  --zone europe-west1-b \
  --num-nodes 3 \
  --machine-type e2-standard-4 \
  --enable-autoscaling \
  --min-nodes 2 \
  --max-nodes 10 \
  --workload-pool=my-project.svc.id.goog
```

## Estimation des Coûts Mensuels

| Service | Configuration | Coût mensuel estimé |
| --- | --- | --- |
| GKE (3 nodes e2-standard-4) | 4 vCPU, 16GB RAM | ~300€ |
| Cloud SQL (PostgreSQL) | db-standard-2, 100GB | ~100€ |
| Cloud Load Balancer | 1 règle + trafic | ~30€ |
| Artifact Registry | 50GB stockage | ~5€ |
| Cloud Logging | 50GB/mois | ~25€ |
| Total |  | ~460€/mois |

> Optimisation : Utilisez les instances préemptibles pour les workloads non critiques (économie de 60-80%).


---


# GitHub Actions CI/CD

> Pipeline complet : tests → build Docker → push registry → déploiement GKE avec rollback automatique.


## Pipeline Complet

**`.github/workflows/deploy.yml`**

```yaml
name: Build and Deploy

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

env:
  PROJECT_ID: ${{ secrets.GCP_PROJECT_ID }}
  GKE_CLUSTER: ecommerce-cluster
  GKE_ZONE: europe-west1-b
  REGISTRY: gcr.io

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: maven
      
      - name: Run tests
        run: mvn verify
      
      - name: Upload coverage
        uses: codecov/codecov-action@v4

  build-and-push:
    needs: test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    
    strategy:
      matrix:
        service: [auth-service, order-service, payment-service, inventory-service]
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Auth to GCP
        uses: google-github-actions/auth@v2
        with:
          credentials_json: ${{ secrets.GCP_SA_KEY }}
      
      - name: Configure Docker
        run: gcloud auth configure-docker
      
      - name: Build and push
        run: |
          docker build -t $REGISTRY/$PROJECT_ID/${{ matrix.service }}:${{ github.sha }} \
            ./backend/${{ matrix.service }}
          docker push $REGISTRY/$PROJECT_ID/${{ matrix.service }}:${{ github.sha }}

  deploy:
    needs: build-and-push
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Auth to GCP
        uses: google-github-actions/auth@v2
        with:
          credentials_json: ${{ secrets.GCP_SA_KEY }}
      
      - name: Get GKE credentials
        uses: google-github-actions/get-gke-credentials@v2
        with:
          cluster_name: ${{ env.GKE_CLUSTER }}
          location: ${{ env.GKE_ZONE }}
      
      - name: Deploy to GKE
        run: |
          for service in auth-service order-service payment-service inventory-service; do
            kubectl set image deployment/$service \
              $service=$REGISTRY/$PROJECT_ID/$service:${{ github.sha }}
          done
      
      - name: Verify deployment
        run: |
          kubectl rollout status deployment/order-service --timeout=300s
```

## Stratégie de Déploiement

```
Push main → [Tests] → Tests OK?
                          ↓ OUI
                   [Build images]
                          ↓
                   [Push Registry]
                          ↓
                   [Deploy GKE]
                          ↓
                   [Rollout status] → Healthy?
                                          ↓ NON
                                     [Rollback auto]
                                          ↓ OUI
                                     [✅ Déploiement réussi]
```


---


# Questions d'Entretien

> Préparez-vous aux questions techniques sur l'architecture, Kafka, Kubernetes et les performances.


## Architecture & Design

#### ❓ Pourquoi des microservices plutôt qu'un monolithe ?

| Niveau | Réponse attendue |
| --- | --- |
| Junior | "Pour séparer les responsabilités" |
| Senior | "Scaling indépendant, déploiements découplés, ownership par équipe. Mais complexité opérationnelle accrue." |
| Architecte | "Dépend du contexte. Monolithe modulaire pour démarrer, extraction vers microservices quand les frontières métier sont claires et que l'équipe/charge le justifient." |


#### ❓ Comment garantir la cohérence des données entre services ?

- Eventual consistency avec événements Kafka
- Saga pattern pour transactions distribuées
- Idempotence des consommateurs
- Outbox pattern pour atomicité event/DB


## Kafka

#### ❓ Comment gérer les messages en erreur ?

- Retry avec backoff exponentiel ( @RetryableTopic )
- Dead Letter Topic après N échecs
- Monitoring du lag et du DLT
- Process manuel ou automatique de replay


#### ❓ Comment garantir l'ordre des messages ?

- Même clé de partition pour messages liés (ex: userId )
- Consumer unique par partition dans un consumer group
- ⚠️ Le scaling horizontal peut casser l'ordre entre partitions


## Kubernetes

#### ❓ Différence entre liveness et readiness probe ?

| Probe | Rôle | Si échec |
| --- | --- | --- |
| Liveness | "Le container est-il vivant ?" | Restart du container |
| Readiness | "Le container peut-il recevoir du trafic ?" | Retrait du load balancer |


#### ❓ Comment débugger un pod qui crash loop ?

**`Debug kubectl`**

```bash
kubectl describe pod <name>      # Voir les events
kubectl logs <name> --previous   # Logs du container précédent
kubectl get events --sort-by=.lastTimestamp
```


## Performance

#### ❓ Comment optimiser une API Spring Boot lente ?

- Profiler avec async-profiler ou JFR
- Vérifier les requêtes SQL (N+1 ?)
- Activer le cache ( @Cacheable )
- Virtual threads pour I/O blocking (Java 21+)
- Connection pool correctement dimensionné (HikariCP)


## Conclusion & Prochaines Étapes

> Ce tutoriel vous a guidé à travers la construction d'une plateforme e-commerce moderne. Les concepts clés :
> •
> Architecture événementielle
> : découplage et résilience
> •
> Microservices
> : autonomie et scalabilité ciblée
> •
> Kubernetes
> : orchestration et auto-scaling
> •
> Observabilité
> : visibilité sur le système distribué

- **🔄 Pattern Saga** — Implémenter pour les transactions distribuées
- **⚡ Redis Cache** — Ajouter du caching distribué
- **🛡️ Circuit Breaker** — Implémenter avec Resilience4j
- **🔥 Tests de charge** — Mettre en place avec Gatling


---
