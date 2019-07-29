# Ошибка разбора исходного кода

Ошибка возникает если исходный код модуля написан с нарушением синтаксиса языка или при 
неправильном использовании инструкций препроцессора.

3\. Не следует разрывать инструкциями препроцессора и областями отдельные грамматические конструкции, выражения, 
а также объявления и места вызова процедур и функций.

Неправильно:

```bsl
Процедура Пример1()
  а = 1
#Область ИмяОбласти
    + 2;
#КонецОбласти // разрыв выражения
КонецПроцедуры

#Область ИмяОбласти
Процедура Пример2()
    // ...
#КонецОбласти // разрыв процедуры
КонецПроцедуры

Если <...> Тогда
    // ...
#Если ВебКлиент Тогда // разрыв блока Если
Иначе
    // ...
#КонецЕсли
КонецЕсли;

Результат = Пример4(Параметр1, 
#Если Клиент Тогда
  Параметр2, // некорректный вызов функции
#КонецЕсли
  Параметр3);
```

Источник: [Стандарт: #439 Использование директив компиляции и инструкций препроцессора](https://its.1c.ru/db/v8std#content:439:hdoc)
