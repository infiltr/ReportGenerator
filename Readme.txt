Основной класс программы ReportGenerator. Запускается согласно заданию.
В классе ConfigReader происходит считывание и хранение параметров из xml файла. Установлены дополнительные ограничения на параметры. Ширина первого поля не 
обрабатывается, подразумеваем, что она задана корректно. Второе поле не должно быть уже 4 единиц, ширина третьего поля не уже 5 единиц. ширина всей страницы
не должна быть уже суммы 3 полей. Минимальная высота страницы не может быть меньше 4.  
В методе getData класса SourceReader происходит считывание данных из sorce файла. Предоставляет набор get'теров, для получения параметров.
Класс Hyphenator был найден в интернете, используется для разбития слов на слоги.
--------------------------------------------
Основной алгоритм реализован в ReportWriter.
	1) Разбитие текущего значения из source на лексемы (дата разбивается по "/", имя - по любым не буквенным символам) 
	2) Каждая лексема разбивается на слоги с помощью класса Hyphenator
	3) Создаётся "матрица" размером Nx3, 3 - число столбцов, N - число строк, необходимое для хранения данной записи.
	4) Данные заносятся в матрицу последовательно из списка лексем (теперь лексема - это слоги+разделители) до тех пор, пока не заполнится строка
	5) Если необходимо - добавляются разделители страниц и печатаются title'ы
В целом, весь код старался делать самодокументируемым + вставлял javadoc'и.  