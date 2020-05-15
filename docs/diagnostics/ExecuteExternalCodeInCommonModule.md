# Выполнение произвольного кода в общем модуле на сервере (ExecuteExternalCodeInCommonModule)

| Тип | Поддерживаются<br/>языки | Важность | Включена<br/>по умолчанию | Время на<br/>исправление (мин) | Тэги |
| :-: | :-: | :-: | :-: | :-: | :-: |
| `Потенциальная уязвимость` | `BSL` | `Критичный` | `Да` | `15` | `badpractice`<br/>`standard` |

<!-- Блоки выше заполняются автоматически, не трогать -->
## Описание диагностики
<!-- Описание диагностики заполняется вручную. Необходимо понятным языком описать смысл и схему работу -->

При разработке решений следует учитывать, что опасно использование не только непосредственного выполнения кода, написанного в режиме Предприятие, но и алгоритмов, где методами `Выполнить` или `Вычислить` исполняется код в серверных функциях и процедурах.  
Если выполнение произвольного кода необходимо, то такой код должен предварительно располагаться в общем модуле и пройти аудит.

**Ограничение не распространяется на код, выполняемый на клиенте.**

## Примеры
<!-- В данном разделе приводятся примеры, на которые диагностика срабатывает, а также можно привести пример, как можно исправить ситуацию -->

## Источники
<!-- Необходимо указывать ссылки на все источники, из которых почерпнута информация для создания диагностики -->
<!-- Примеры источников

* Источник: [Стандарт: Тексты модулей](https://its.1c.ru/db/v8std#content:456:hdoc)
* Полезная информаця: [Отказ от использования модальных окон](https://its.1c.ru/db/metod8dev#content:5272:hdoc)
* Источник: [Cognitive complexity, ver. 1.4](https://www.sonarsource.com/docs/CognitiveComplexity.pdf) -->

* [Ограничения на использование Выполнить и Вычислить на сервере](https://its.1c.ru/db/v8std#content:770:hdoc)

## Сниппеты

<!-- Блоки ниже заполняются автоматически, не трогать -->
### Экранирование кода

```bsl
// BSLLS:ExecuteExternalCodeInCommonModule-off
// BSLLS:ExecuteExternalCodeInCommonModule-on
```

### Параметр конфигурационного файла

```json
"ExecuteExternalCodeInCommonModule": false
```