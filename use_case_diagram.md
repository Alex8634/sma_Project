# Use Case Diagram - SmaMySuperAlarm

## Detailed Use Case Diagram with Extend/Include Relationships

```mermaid
graph TB
    %% Actors as stickmen (using Unicode characters)
    User((👤 User))
    Smartwatch((⌚ Smartwatch))
    System((💻 System))
    
    %% Basic Use Cases (Primary Actions)
    UC1[Set Regular Alarm]
    UC2[Set Super Alarm]
    UC3[Snooze Alarm]
    UC4[Stop Alarm]
    UC5[Monitor Heart Rate]
    UC6[Configure Sleep Schedule]
    UC7[Manage Preferences]
    
    %% Secondary Use Cases (Included/Extended)
    UC8[Solve Puzzle]
    UC9[Play Game]
    UC10[Enter Password]
    UC11[Validate Time Input]
    UC12[Save Alarm Settings]
    UC13[Check Heart Rate Threshold]
    UC14[Vibrate Watch]
    UC15[Send Stop Signal]
    
    %% Relationships - Basic Actions
    User --> UC1
    User --> UC2
    User --> UC3
    User --> UC4
    User --> UC6
    User --> UC7
    
    Smartwatch --> UC5
    Smartwatch --> UC14
    
    System --> UC11
    System --> UC12
    System --> UC13
    System --> UC15
    
    %% Include Relationships (dashed lines with <<include>>)
    UC2 -.->|<<include>> UC8
    UC2 -.->|<<include>> UC9
    UC2 -.->|<<include>> UC10
    UC1 -.->|<<include>> UC11
    UC2 -.->|<<include>> UC11
    UC1 -.->|<<include>> UC12
    UC2 -.->|<<include>> UC12
    UC6 -.->|<<include>> UC12
    UC7 -.->|<<include>> UC12
    UC5 -.->|<<include>> UC13
    UC13 -.->|<<include>> UC14
    UC13 -.->|<<include>> UC15
    
    %% Extend Relationships (dotted lines with <<extend>>)
    UC3 -.->|<<extend>> UC1
    UC3 -.->|<<extend>> UC2
    UC4 -.->|<<extend>> UC1
    UC4 -.->|<<extend>> UC2
    UC15 -.->|<<extend>> UC4
    
    %% Styling
    classDef actor fill:#e1f5fe,stroke:#01579b,stroke-width:3px
    classDef basicUseCase fill:#c8e6c9,stroke:#2e7d32,stroke-width:2px
    classDef secondaryUseCase fill:#fff3e0,stroke:#ef6c00,stroke-width:2px
    
    class User,Smartwatch,System actor
    class UC1,UC2,UC3,UC4,UC5,UC6,UC7 basicUseCase
    class UC8,UC9,UC10,UC11,UC12,UC13,UC14,UC15 secondaryUseCase
```

## Use Case Descriptions

### Primary Actors
- **👤 User**: The person using the alarm app on their phone
- **⌚ Smartwatch**: The wearable device that monitors heart rate and provides feedback
- **💻 System**: The Android alarm application system that manages data and logic

### Basic Use Cases (Primary Actions)

1. **Set Regular Alarm**: User creates a basic alarm with time selection
2. **Set Super Alarm**: User creates an alarm with security features enabled
3. **Snooze Alarm**: User temporarily postpones an active alarm
4. **Stop Alarm**: User completely stops an active alarm
5. **Monitor Heart Rate**: Smartwatch continuously tracks user's heart rate
6. **Configure Sleep Schedule**: User sets their default sleep hours
7. **Manage Preferences**: User adjusts app settings and configurations

### Secondary Use Cases (Included/Extended)

8. **Solve Puzzle**: Security mechanism included in Super Alarm
9. **Play Game**: Alternative security mechanism included in Super Alarm
10. **Enter Password**: Password verification included in Super Alarm
11. **Validate Time Input**: Ensures time selection is valid (included in alarm setting)
12. **Save Alarm Settings**: Persists alarm configuration to database
13. **Check Heart Rate Threshold**: Evaluates if BPM exceeds 100
14. **Vibrate Watch**: Smartwatch provides haptic feedback
15. **Send Stop Signal**: System communicates stop command to phone

### Relationship Types

#### Include Relationships (<<include>>)
- **Mandatory**: The included use case must be performed
- **Dependency**: The base use case depends on the included use case
- **Reuse**: Included use cases can be reused across multiple base use cases

#### Extend Relationships (<<extend>>)
- **Optional**: The extending use case is optional
- **Conditional**: May or may not be performed based on conditions
- **Enhancement**: Adds additional functionality to the base use case

### Key Relationships Explained

1. **Super Alarm includes security mechanisms**: Every Super Alarm must have either puzzle, game, or password verification
2. **Alarm setting includes validation**: Both regular and super alarms validate time input
3. **All configurations include saving**: Settings are persisted to the database
4. **Heart rate monitoring includes threshold checking**: Continuously evaluates BPM against the 100 threshold
5. **Snooze extends alarms**: Can be applied to both regular and super alarms
6. **Stop alarm extends alarms**: Can stop both types of alarms
7. **Heart rate stop extends stop alarm**: Provides automatic stopping mechanism 