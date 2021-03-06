Функция Пример()
КонецФункции

// Описание вроде
Функция Пример1()
КонецФункции

// Описание вроде
// Возвращаемое значение:
Функция Пример2()
КонецФункции

// Описание вроде
// Возвращаемое значение:
// Строка - строка типа
Функция Пример3()
КонецФункции

// Описание вроде
// Возвращаемое значение:
// Строка - строка типа
Процедура Пример4()
КонецПроцедуры

// Описание вроде
Процедура Пример5()
КонецПроцедуры

// Описание вроде
// Возвращаемое значение:
// Строка
Функция Пример6()
КонецФункции

// Описание вроде
// Возвращаемое значение:
// - Строка
// - булево
// - Неопределено - если неизвестно
Функция Пример7()
КонецФункции

// Параметры подключения к администрируемой информационной базе кластера.
//
// Возвращаемое значение:
//  Структура:
//    * ИмяВКластере - Строка - имя администрируемой информационной базы в кластере серверов,
//    * ИмяАдминистратораИнформационнойБазы - Строка - имя пользователя информационной базы с правами
//                  администратора (если для информационной базы не задан список пользователей ИБ - используется
//                  пустая строка),
//    * ПарольАдминистратораИнформационнойБазы - Строка - пароль пользователя информационной базы
//                  с правами администратора (если для информационной базы не задан список пользователей ИБ или
//                  для пользователя ИБ не установлен пароль - используется пустая строка).
//
Функция ПараметрыАдминистрированияИнформационнойБазыКластера() Экспорт

	Результат = Новый Структура();

	Результат.Вставить("ИмяВКластере", "");
	Результат.Вставить("ИмяАдминистратораИнформационнойБазы", "");
	Результат.Вставить("ПарольАдминистратораИнформационнойБазы", "");

	Возврат Результат;

КонецФункции

// См. Пример7()
Функция Пример8()
КонецФункции

// См. Пример7()
//
//
//
Функция Пример9()
КонецФункции

// Загружает настройку из хранилища общих настроек, как метод платформы Загрузить,
// объектов СтандартноеХранилищеНастроекМенеджер или ХранилищеНастроекМенеджер.<Имя хранилища>,
// но с поддержкой длины ключа настроек более 128 символов путем хеширования части,
// которая превышает 96 символов.
// Кроме того, возвращает указанное значение по умолчанию, если настройки не существуют.
// Если нет права СохранениеДанныхПользователя, возвращается значение по умолчанию без ошибки.
//
// В возвращаемом значении очищаются ссылки на несуществующий объект в базе данных, а именно
// - возвращаемая ссылка заменяется на указанное значение по умолчанию;
// - из данных типа Массив ссылки удаляются;
// - у данных типа Структура и Соответствие ключ не меняется, а значение устанавливается Неопределено;
// - анализ значений в данных типа Массив, Структура, Соответствие выполняется рекурсивно.
//
// Параметры:
//   КлючОбъекта          - Строка           - см. синтакс-помощник платформы.
//   КлючНастроек         - Строка           - см. синтакс-помощник платформы.
//   ЗначениеПоУмолчанию  - Произвольный     - значение, которое возвращается, если настройки не существуют.
//                                             Если не указано, возвращается значение Неопределено.
//   ОписаниеНастроек     - ОписаниеНастроек - см. синтакс-помощник платформы.
//   ИмяПользователя      - Строка           - см. синтакс-помощник платформы.
//
// Возвращаемое значение:
//   Произвольный - см. синтакс-помощник платформы.
//
Функция BUG_1490(КлючОбъекта, КлючНастроек, ЗначениеПоУмолчанию = Неопределено,
			ОписаниеНастроек = Неопределено, ИмяПользователя = Неопределено) Экспорт
КонецФункции

// Запустить выполнение процедуры в фоновом задании, если это возможно.
//
// При выполнении любого из следующих условий запуск выполняется не в фоне, а сразу в основном потоке:
//  * если вызов выполняется в файловой базе во внешнем соединении (в этом режиме фоновые задания не поддерживаются);
//  * если приложение запущено в режиме отладки (параметр /C РежимОтладки) - для упрощения отладки конфигурации;
//  * если в файловой ИБ имеются активные фоновые задания - для снижения времени ожидания пользователя;
//  * если выполняется процедура модуля внешней обработки или внешнего отчета.
//
// Не следует использовать эту функцию, если необходимо безусловно запускать фоновое задание.
// Может применяться совместно с функцией ДлительныеОперацииКлиент.ОжидатьЗавершение.
//
// Вызываемая процедура может быть с произвольным числом параметров, но не более 7.
// Значения передаваемых параметров процедуры, а также возвращаемое значение должны быть сериализуемыми.
// Параметры процедуры не должны быть возвращаемыми.
//
// Параметры:
//
//  ПараметрыВыполнения - см. ДлительныеОперации.ПараметрыВыполненияПроцедуры
//
//  ИмяПроцедуры - Строка - имя экспортной процедуры общего модуля, модуля менеджера объекта
//                          или модуля обработки, которую необходимо выполнить в фоне.
//                          Например, "МойОбщийМодуль.МояПроцедура", "Отчеты.ЗагруженныеДанные.Сформировать"
//                          или "Обработки.ЗагрузкаДанных.МодульОбъекта.Загрузить".
//
//  Параметр1 - Произвольный - произвольные параметры вызова процедуры. Количество параметров может быть от 0 до 7.
//  Параметр2 - Произвольный
//  Параметр3 - Произвольный
//  Параметр4 - Произвольный
//  Параметр5 - Произвольный
//  Параметр6 - Произвольный
//  Параметр7 - Произвольный
//
// Возвращаемое значение:
//  Структура - параметры выполнения задания:
//   * Статус               - Строка - "Выполняется", если задание еще не завершилось;
//                                     "Выполнено", если задание было успешно выполнено;
//                                     "Ошибка", если задание завершено с ошибкой;
//                                     "Отменено", если задание отменено пользователем или администратором.
//   * ИдентификаторЗадания - УникальныйИдентификатор - если Статус = "Выполняется", то содержит
//                                     идентификатор запущенного фонового задания.
//   * КраткоеПредставлениеОшибки   - Строка - краткая информация об исключении, если Статус = "Ошибка".
//   * ПодробноеПредставлениеОшибки - Строка - подробная информация об исключении, если Статус = "Ошибка".
//   * Сообщения - ФиксированныйМассив - если Статус <> "Выполняется", то массив объектов СообщениеПользователю,
//                                      которые были сформированы в фоновом задании.
//
// Пример:
//  В общем виде процесс запуска и обработки результата длительной операции в модуле формы выглядит следующим образом:
//
//   1) Процедура, которая будет исполняться в фоне, располагается в модуле менеджера объекта или в серверном общем модуле:
//    Процедура ВыполнитьРасчет(Знач МойПараметр1, Знач МойПараметр2) Экспорт
//     ...
//    КонецПроцедуры
//
//   2) Запуск операции на сервере и подключение обработчика ожидания (при необходимости):
//    &НаКлиенте
//    Процедура ВыполнитьРасчет()
//     ДлительнаяОперация = НачатьВыполнениеНаСервере();
//     ОповещениеОЗавершении = Новый ОписаниеОповещения("ОбработатьРезультат", ЭтотОбъект);
//     ПараметрыОжидания = ДлительныеОперацииКлиент.ПараметрыОжидания(ЭтотОбъект);
//     ДлительныеОперацииКлиент.ОжидатьЗавершение(ДлительнаяОперация, ОповещениеОЗавершении, ПараметрыОжидания);
//    КонецПроцедуры
//
//    &НаСервере
//    Функция НачатьВыполнениеНаСервере()
//     Возврат ДлительныеОперации.ВыполнитьПроцедуру(, "Обработки.МояОбработка.ВыполнитьРасчет",
//      МойПараметр1, МойПараметр2);
//    КонецФункции
//
//   3) Обработка результата длительной операции:
//    &НаКлиенте
//    Процедура ОбработатьРезультат(Результат, ДополнительныеПараметры) Экспорт
//     Если Результат = Неопределено Тогда
//      Возврат;
//     КонецЕсли;
//     ПриЗавершенииРасчета();
//    КонецПроцедуры
//
Функция BUG_1488_1(Знач ПараметрыВыполнения = Неопределено, ИмяПроцедуры, Знач Параметр1 = Неопределено,
    Знач Параметр2 = Неопределено, Знач Параметр3 = Неопределено, Знач Параметр4 = Неопределено,
    Знач Параметр5 = Неопределено, Знач Параметр6 = Неопределено, Знач Параметр7 = Неопределено, Знач Параметр8 = Неопределено)
КонецФункции

// Инициализирует структуру параметров для взаимодействия с файловой системой.
//
// Параметры:
//  РежимДиалога - РежимДиалогаВыбораФайла - режим работы конструируемого диалога выбора файлов.
//
// Возвращаемое значение:
//   см. ФайловаяСистемаКлиент.ПараметрыЗагрузкиФайла
//
Функция BUG_1488_2(РежимДиалога)
КонецФункции