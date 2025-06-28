# SmaMySuperAlarm App Architecture Block Diagram

## High-Level System Architecture

```mermaid
graph TB
    subgraph "User Interface Layer"
        A[MainActivity1 - User Registration]
        B[MainActivity2 - Welcome Screen]
        C[MainActivity3 - Main Menu]
        D[MainActivity4 - SuperAlarm Configuration]
        E[MainActivity5 - Preferences]
        F[MainActivity6 - Alarm Selection]
        G[MainActivity - Normal Alarm]
        H[GameActivity - Circle Game]
    end

    subgraph "Business Logic Layer"
        I[AlarmManager Service]
        J[WearableListenerService]
        K[AlarmReceiver - Broadcast Receiver]
        L[UserRepository]
        M[Wearable Communication]
    end

    subgraph "Data Layer"
        N[Room Database]
        O[SharedPreferences]
        P[Firebase Database]
    end

    subgraph "External Services"
        Q[Wear OS Device]
        R[Firebase Cloud]
        S[Android System Services]
    end

    %% User Flow Connections
    A --> B
    B --> C
    C --> D
    C --> E
    C --> F
    F --> G
    F --> D

    %% Business Logic Connections
    D --> I
    G --> I
    I --> K
    K --> H
    D --> J
    J --> M
    M --> Q

    %% Data Layer Connections
    A --> L
    E --> L
    L --> N
    D --> O
    E --> O
    J --> P
    P --> R

    %% System Services
    I --> S
    K --> S
    J --> S

    classDef uiLayer fill:#e1f5fe
    classDef businessLayer fill:#f3e5f5
    classDef dataLayer fill:#e8f5e8
    classDef externalLayer fill:#fff3e0

    class A,B,C,D,E,F,G,H uiLayer
    class I,J,K,L,M businessLayer
    class N,O,P dataLayer
    class Q,R,S externalLayer
```

## Detailed Component Architecture

```mermaid
graph TB
    subgraph "Activities & UI Components"
        subgraph "User Management"
            A1[MainActivity1<br/>User Registration]
            A2[MainActivity2<br/>Welcome Screen]
            A3[MainActivity3<br/>Main Menu]
        end

        subgraph "Alarm Management"
            A4[MainActivity4<br/>SuperAlarm Config]
            A5[MainActivity6<br/>Alarm Selection]
            A6[MainActivity<br/>Normal Alarm]
        end

        subgraph "Settings & Games"
            A7[MainActivity5<br/>Preferences]
            A8[GameActivity<br/>Circle Game]
        end
    end

    subgraph "Services & Receivers"
        subgraph "Alarm Services"
            S1[AlarmReceiver<br/>Broadcast Receiver]
            S2[AlarmManager<br/>System Service]
        end

        subgraph "Wearable Services"
            S3[WearableListenerService<br/>Message Listener]
            S4[WearableMessageService<br/>Communication]
        end
    end

    subgraph "Data Management"
        subgraph "Database Layer"
            D1[AppDatabase<br/>Room Database]
            D2[UserDao<br/>Data Access Object]
            D3[UserRepository<br/>Repository Pattern]
        end

        subgraph "Local Storage"
            D4[SharedPreferences<br/>Settings Storage]
            D5[User Model<br/>Data Entity]
        end

        subgraph "Cloud Services"
            D6[Firebase Database<br/>BPM Data]
            D7[Firebase Auth<br/>User Authentication]
        end
    end

    subgraph "External Systems"
        E1[Wear OS Device<br/>Smartwatch]
        E2[Android System<br/>AlarmManager]
        E3[Firebase Cloud<br/>Real-time Data]
    end

    %% User Flow
    A1 --> A2 --> A3
    A3 --> A4
    A3 --> A5
    A3 --> A7
    A5 --> A6
    A5 --> A4

    %% Alarm Flow
    A4 --> S1
    A6 --> S1
    S1 --> S2
    S1 --> A8

    %% Wearable Flow
    A4 --> S3
    S3 --> S4
    S4 --> E1
    E1 --> S3

    %% Data Flow
    A1 --> D3
    A7 --> D3
    D3 --> D2
    D2 --> D1
    A4 --> D4
    A7 --> D4
    S3 --> D6
    D6 --> E3

    %% System Integration
    S2 --> E2
    S1 --> E2

    classDef activity fill:#e3f2fd
    classDef service fill:#f3e5f5
    classDef data fill:#e8f5e8
    classDef external fill:#fff3e0

    class A1,A2,A3,A4,A5,A6,A7,A8 activity
    class S1,S2,S3,S4 service
    class D1,D2,D3,D4,D5,D6,D7 data
    class E1,E2,E3 external
```

## Data Flow Architecture

```mermaid
flowchart TD
    subgraph "User Input Flow"
        U1[User Registration] --> U2[Profile Data]
        U2 --> U3[Database Storage]
        U4[Alarm Configuration] --> U5[Alarm Settings]
        U5 --> U6[System Scheduling]
    end

    subgraph "Alarm Execution Flow"
        A1[Alarm Trigger] --> A2[AlarmReceiver]
        A2 --> A3[Sound Playback]
        A3 --> A4[Deactivation Challenge]
        A4 --> A5[Password/Puzzle/Game]
        A5 --> A6[Alarm Stop]
    end

    subgraph "Wearable Integration Flow"
        W1[Wearable Selection] --> W2[Connection Check]
        W2 --> W3[Permission Request]
        W3 --> W4[Heart Rate Monitoring]
        W4 --> W5[BPM Threshold Check]
        W5 --> W6[Auto Stop Alarm]
    end

    subgraph "Data Persistence Flow"
        D1[User Actions] --> D2[Repository]
        D2 --> D3[Room Database]
        D3 --> D4[SharedPreferences]
        D5[Wearable Data] --> D6[Firebase]
        D6 --> D7[Real-time Sync]
    end

    %% Cross-flow connections
    U3 --> A1
    U6 --> A1
    W6 --> A6
    D4 --> U4
    D7 --> W5

    classDef userFlow fill:#e1f5fe
    classDef alarmFlow fill:#f3e5f5
    classDef wearableFlow fill:#e8f5e8
    classDef dataFlow fill:#fff3e0

    class U1,U2,U3,U4,U5,U6 userFlow
    class A1,A2,A3,A4,A5,A6 alarmFlow
    class W1,W2,W3,W4,W5,W6 wearableFlow
    class D1,D2,D3,D4,D5,D6,D7 dataFlow
```

## Component Dependencies

```mermaid
graph LR
    subgraph "Core Dependencies"
        CD1[Android SDK]
        CD2[Kotlin]
        CD3[AndroidX Libraries]
    end

    subgraph "Database Dependencies"
        DD1[Room Database]
        DD2[KSP Compiler]
        DD3[Coroutines]
    end

    subgraph "Wearable Dependencies"
        WD1[Wear OS API]
        WD2[Google Play Services]
        WD3[Message Client]
    end

    subgraph "Cloud Dependencies"
        FD1[Firebase Database]
        FD2[Firebase Auth]
        FD3[Real-time Listener]
    end

    subgraph "UI Dependencies"
        UD1[Material Design]
        UD2[View Binding]
        UD3[Constraint Layout]
    end

    %% Dependency relationships
    CD1 --> DD1
    CD2 --> DD2
    CD3 --> DD3
    CD1 --> WD1
    WD1 --> WD2
    WD2 --> WD3
    CD1 --> FD1
    FD1 --> FD2
    FD2 --> FD3
    CD3 --> UD1
    UD1 --> UD2
    UD2 --> UD3

    classDef core fill:#e3f2fd
    classDef database fill:#f3e5f5
    classDef wearable fill:#e8f5e8
    classDef cloud fill:#fff3e0
    classDef ui fill:#fce4ec

    class CD1,CD2,CD3 core
    class DD1,DD2,DD3 database
    class WD1,WD2,WD3 wearable
    class FD1,FD2,FD3 cloud
    class UD1,UD2,UD3 ui
```

## Key Features by Component

### Activities
- **MainActivity1**: User registration and profile creation
- **MainActivity2**: Welcome screen after registration
- **MainActivity3**: Main menu with navigation options
- **MainActivity4**: SuperAlarm configuration with wearable integration
- **MainActivity5**: User preferences and settings management
- **MainActivity6**: Alarm type selection (normal vs SuperAlarm)
- **MainActivity**: Standard alarm functionality
- **GameActivity**: Interactive alarm deactivation game

### Services
- **AlarmReceiver**: Handles alarm triggers and sound playback
- **WearableListenerService**: Manages wearable device communication
- **AlarmManager**: System-level alarm scheduling

### Data Layer
- **Room Database**: Local data persistence for user profiles
- **SharedPreferences**: Settings and configuration storage
- **Firebase Database**: Real-time BPM data from wearable devices
- **UserRepository**: Data access abstraction layer

### External Integrations
- **Wear OS**: Smartwatch communication and heart rate monitoring
- **Firebase**: Cloud-based real-time data synchronization
- **Android System**: Alarm scheduling and notification services

This architecture follows the MVVM (Model-View-ViewModel) pattern with Repository pattern for data management, ensuring clean separation of concerns and maintainable code structure. 