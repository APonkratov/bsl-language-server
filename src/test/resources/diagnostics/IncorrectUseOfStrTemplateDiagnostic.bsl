Процедура Метод()

    А = СтрШаблон("Наименование (версия %1)"); // ошибка

    Б = СтрШаблон("%1 (версия %2)", Наименование); // ошибка

    К = СтрШаблон("Наименование %11", Наименование); // ошибка

    К = СтрШаблон("Наименование %0", Наименование); // ошибка

    Ж = СтрШаблон("Наименование %2 (версия %%3)", Наименование, Версия); // ошибка

    //здесь ошибочно не закрыта скобка для НСтр
    В = СтрШаблон(НСтр("ru='Наименование (версия %1)'", Версия())); // ошибка

    НовыйШаблон = "123";
    Н = СтрШаблон(НовыйШаблон, Наименование); // ошибка

    НовыйШаблон1 = "123";
    ДругаяСтрока = "5487";
    Н = СтрШаблон(НовыйШаблон1, Наименование); // ошибка

    //НовыйШаблон2 = НСтр("ru='Наименование (версия)'";
    НовыйШаблон2 = "5487";
    Н = СтрШаблон(НовыйШаблон2, Наименование); // ошибка

    Г = СтрШаблон(НСтр("ru='Наименование (версия %1)'"), Версия());

    Д = СтрШаблон("Наименование (версия)");

    Е = СтрШаблон("Наименование (версия %1)", Наименование);

    Е = СтрШаблон("Наименование %1 (версия %2)", Наименование, Версия);

    З = СтрШаблон("Наименование %%1 (версия %%2)");
    Ий = СтрШаблон("Наименование %1 (версия %%2)", Наименование);

    Л = СтрШаблон("Наименование %(1)1", Наименование); // в СП разрешен такой вариант
    Л = СтрШаблон("Наименование %(1)");

    М = СтрШаблон(ШаблонНаименования, Наименование);
    М = СтрШаблон("123" + ШаблонНаименования, Наименование);

    НовыйШаблон3 = "%1";
    Н = СтрШаблон(НовыйШаблон3, Наименование);
КонецПроцедуры