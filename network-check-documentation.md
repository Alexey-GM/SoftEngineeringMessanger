# Документация: Проверка интернет-соединения в Live Player

## Обзор

Фича проверки интернет-соединения позволяет различать проблемы пользователя (слабый/отсутствующий интернет) и проблемы на стороне камеры/сервера (ошибка камеры).

## Архитектура

### Компоненты

1. **LivePlayerFrame** - проверяет силу соединения через Android ConnectivityManager
2. **LivePlayerPresenter** - определяет тип ошибки на основе силы соединения
3. **LivePlayerFragment** - отображает соответствующие сообщения об ошибках

### Логика определения типа ошибки

```
Соединение отсутствует или слабое (< 14500 kbps)
    ↓
WEAK_INTERNET_CONNECTION (проблема у пользователя)
    ↓
Отображение: INTERNET_CONNECTION_ERROR

Соединение сильное (>= 14500 kbps), но поток не идет
    ↓
ERROR (проблема камеры/сервера)
    ↓
Отображение: OPEN_VIDEO_ERROR
```

## Сценарии использования

### 1. Проверка при открытии видео (таймаут)

После `LIVE_PLAYER_TIMEOUT`, если плеер не начал воспроизведение, проверяется соединение.

### 2. Проверка при исчерпании попыток

Если достигнут `MAX_TRIES`, проверяется соединение перед показом ошибки.

### 3. Мониторинг во время воспроизведения

Каждые 5 секунд проверяется состояние плеера. Если плеер остановился 3 раза подряд (15 секунд), проверяется соединение.

## Диаграммы

### Sequence Diagram: Проверка при открытии видео

```mermaid
sequenceDiagram
    participant LP as LivePlayerPresenter
    participant LPF as LivePlayerFrame
    participant CM as ConnectivityManager
    participant View as ILivePlayerView

    Note over LP: openVideo() вызван
    LP->>LP: Запускает таймер (LIVE_PLAYER_TIMEOUT)
    
    alt Плеер не начал воспроизведение за таймаут
        LP->>LP: checkInternetConnection()
        LP->>View: checkNetworkConnectionStrength(callback)
        View->>LPF: checkNetworkConnectionStrength()
        LPF->>CM: getNetworkCapabilities(activeNetwork)
        CM-->>LPF: networkCapabilities
        
        alt Нет сети
            LPF-->>View: ConnectionStrength.NONE
        else Слабая сеть (< 14500 kbps)
            LPF-->>View: ConnectionStrength.WEAK
        else Сильная сеть (>= 14500 kbps)
            LPF-->>View: ConnectionStrength.STRONG
        end
        
        View-->>LP: connectionStrength
        
        alt NONE или WEAK
            LP->>LP: onError(WEAK_INTERNET_CONNECTION)
            LP->>View: changeState(INTERNET_CONNECTION_ERROR)
            LP->>View: setPreviewStatus(WEAK_INTERNET_CONNECTION)
        else STRONG
            LP->>LP: onError(ERROR)
            LP->>View: changeState(OPEN_VIDEO_ERROR)
            LP->>View: setPreviewStatus(ERROR)
        end
    end
```

### Sequence Diagram: Мониторинг во время воспроизведения

```mermaid
sequenceDiagram
    participant LP as LivePlayerPresenter
    participant Playback as Playback
    participant LPF as LivePlayerFrame
    participant CM as ConnectivityManager

    Note over LP: startPlaybackMonitoring() запущен
    loop Каждые 5 секунд
        LP->>Playback: isPlaying()?
        Playback-->>LP: false
        
        alt Плеер остановился (wasPlaying = true)
            LP->>LP: consecutiveFailures++
            
            alt consecutiveFailures >= 3 (15 секунд)
                LP->>LP: checkInternetConnection()
                LP->>LPF: checkNetworkConnectionStrength()
                LPF->>CM: getNetworkCapabilities()
                CM-->>LPF: networkCapabilities
                LPF-->>LP: ConnectionStrength
                
                alt NONE или WEAK
                    LP->>LP: onError(WEAK_INTERNET_CONNECTION)
                else STRONG
                    LP->>LP: onError(ERROR)
                end
                LP->>LP: break (завершение мониторинга)
            end
        else Плеер играет
            LP->>LP: consecutiveFailures = 0
        end
    end
```

### Activity Diagram: Логика определения типа ошибки

```mermaid
flowchart TD
    Start([Проблема с воспроизведением])
    Start --> CheckNetwork{Проверка<br/>соединения}
    
    CheckNetwork --> GetCapabilities[Получить NetworkCapabilities]
    GetCapabilities --> HasNetwork{Сеть<br/>доступна?}
    
    HasNetwork -->|Нет| None[NONE]
    HasNetwork -->|Да| CheckSpeed{Скорость<br/>>= 14500 kbps?}
    
    CheckSpeed -->|Нет| Weak[WEAK]
    CheckSpeed -->|Да| Strong[STRONG]
    
    None --> UserError[WEAK_INTERNET_CONNECTION<br/>Проблема у пользователя]
    Weak --> UserError
    
    Strong --> CameraError[ERROR<br/>Проблема камеры/сервера]
    
    UserError --> ShowUserMsg[Показать:<br/>INTERNET_CONNECTION_ERROR<br/>WEAK_INTERNET_CONNECTION]
    
    CameraError --> ShowCameraMsg[Показать:<br/>OPEN_VIDEO_ERROR<br/>ERROR]
    
    ShowUserMsg --> End([Завершение])
    ShowCameraMsg --> End
    
    style UserError fill:#ffcccc
    style CameraError fill:#ffffcc
    style ShowUserMsg fill:#ffcccc
    style ShowCameraMsg fill:#ffffcc
```

## Ключевые параметры

| Параметр | Значение | Описание |
|----------|----------|----------|
| `MIN_KBPS` | 14500 | Минимальная скорость для определения "сильного" соединения |
| `LIVE_PLAYER_TIMEOUT` | Из `PlayerConst` | Таймаут ожидания начала воспроизведения |
| `MAX_TRIES` | Из `ABasePlayerPresenter` | Максимальное количество попыток открытия видео |
| Мониторинг интервал | 5000 мс | Интервал проверки состояния плеера |
| Последовательные сбои | 3 | Количество сбоев подряд перед проверкой сети |

## Типы ошибок

### ConnectionStrength (enum)
- `NONE` - соединение отсутствует
- `WEAK` - слабое соединение (< 14500 kbps)
- `STRONG` - сильное соединение (>= 14500 kbps)

### Camera.Status (результирующие статусы)
- `WEAK_INTERNET_CONNECTION` - проблема у пользователя
- `ERROR` - проблема с камерой/сервером

### PlayerConst.State (состояния UI)
- `INTERNET_CONNECTION_ERROR` - отображается при WEAK_INTERNET_CONNECTION
- `OPEN_VIDEO_ERROR` - отображается при ERROR

## Примеры использования

### Пример 1: Пользователь потерял интернет
```
1. Плеер играет нормально
2. Пользователь отключает Wi-Fi
3. Через 15 секунд (3 проверки × 5 сек) мониторинг обнаруживает остановку
4. Проверка соединения → NONE
5. Показывается: INTERNET_CONNECTION_ERROR
```

### Пример 2: Камера недоступна, интернет в порядке
```
1. Пользователь пытается открыть видео
2. Таймаут истекает, плеер не начал воспроизведение
3. Проверка соединения → STRONG (14500+ kbps)
4. Показывается: OPEN_VIDEO_ERROR (проблема камеры)
```

### Пример 3: Слабое соединение
```
1. Пользователь на мобильном интернете (3G)
2. Скорость < 14500 kbps
3. Плеер не может воспроизвести поток
4. Проверка соединения → WEAK
5. Показывается: INTERNET_CONNECTION_ERROR
```

## Реализация

### LivePlayerFrame.checkNetworkConnectionStrength()
```kotlin
// Проверяет силу соединения через ConnectivityManager
// Возвращает: ConnectionStrength (NONE, WEAK, STRONG)
```

### LivePlayerPresenter.checkInternetConnection()
```kotlin
// Определяет тип ошибки на основе силы соединения
// NONE/WEAK → WEAK_INTERNET_CONNECTION
// STRONG → ERROR
```

### LivePlayerPresenter.startPlaybackMonitoring()
```kotlin
// Мониторит состояние воспроизведения каждые 5 секунд
// При 3 последовательных сбоях → проверка соединения
```

