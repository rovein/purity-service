import i18n from "i18next";
import LanguageDetector from "i18next-browser-languagedetector";

i18n.use(LanguageDetector).init({
  // we init with resources
  resources: {
    EN: {
      translations: {
        Login: "Login",
        Signin: "Sign in",
        Signup:"Sign up",
        Comp: "Placement owner",
        CComp:"Cleaning provider",
        Name: "Name",
        Email: "Email",
        Password: "Password",
        Profile: "Profile",
        Actions: "Actions",
        Type: "Type",
        WCount: "Windows count",
        Floor: "Floor",
        Area: "Area",
        Date:"Date of last cleaning",
        Measure: "m²",
        Rooms:"Placements",
        Signout: "Sign out",
        AddR: "Add placement",
        EditR: "Edit placement",
        EditP:"Edit profile",
        EditS:"Edit service",
        More:"More information",
        Edit:"Edit",
        PasswordChangeMsg: 'Leave this fields empty if you don`t want to the update password',
        Delete:"Delete",
        Cancel: "Cancel",
        Failiture:"Failure",
        Loading:"Loading",
        Statistics:"Statistics",
        Backup:"Database backup",
        EConfirmPass: "Passwords do not match.",
        Admin:"Admin",   
        Save:"Save",
        Phone:"Phone number",
        Address:"Address",
        City:" ",
        Street:"St ",
        House: " ",
        FCountry:"Country",
        FCity:"City",
        FStreet:"Street",
        FHouse: "House number",
        Services:"Cleaning services",
        AddS:"Add service",
        Desc:"Description",
        Price:"Price",
        minA:"Minimum area",
        maxA:"Maximum area",
        rType:"Room type",
        pMes:"₴",
        AirQ:"Air quality",
        Hum:"Humidity",
        AdjFact:"Adjustment factor",
        Prior:"Priority",
        Add: "Add",
        sId:"Service id",
        serviceName:"Service",
        rId:'Room number',
        CDate:'Date of concluding the contract',
        Contract:'Contract',
        Contracts:'Contracts',
        SContract:"Sign a contract",
        pickSId: "Pick service id:",
        pickRId:"Pick room number:",
        selectService: "Select service",
        selectRoom: "Select placement",
        selectDate: "Select date",
        accIsLocked: "Account is locked.",
        LockUser: "Lock",
        UnlockUser: "Unlock",
        IsLocked: "Is locked",

        DName: "Name",
        checkCred: "Check the correctness of the entered data!",
        EName:"Your name format is wrong! Please try again.",
        EEmail:"Your email format is wrong! Please try again.",
        EPass:"Your password format is wrong! Please try again.",
        EPhone:"Your phone number format is wrong! Please try again.",
        ECountry:"Your country format is wrong! Please try again.",
        ECity:"Your city format is wrong! Please try again.",
        EStreet:"Your street format is wrong! Please try again.",
        EHouse:"Your house format is wrong! Please try again.",
        EType:"Your type format is wrong! Please try again.",
        EFloor:"Your floor format is wrong! Please try again.",
        EWCount:"Your windows number format is wrong! Please try again.",
        EArea:"Your area format is wrong! Please try again.",
        EDesc:"Your description format is wrong! Please try again.",
        EMinA:"Your minimum area format is wrong! Please try again.",
        EMaxA:"Your maximum area format is wrong! Please try again.",
        EEroor:"Something went wrong! Please? try again.",
        EPPM:"Your price per meter format is wrong! Please try again.",
        PPM:"Price per meter",
        BackupWillDownload: "File for data restore will be downloaded after some seconds...",
        Success: "Success!",
        Search:"Search",
        GlobalPlacementsSearch:"Placements search...",
        eExist:"Such email already exist in system!",
        ConfirmPassword: "Confirm password",
        Temp: "Temperature",
        dFact: "Dirtiness factor",
        ConfigureDevice: "Configure device",
        SuccessConfiguring: "Device has been configured",
        DeviceIp: "Device IP",
        ServerIp: "Server address",
        ServerPort: "Server port",
        PlacementId: "Placement ID",
        Configure: "Configure",
        AreYouSure: "Are you sure?",
        NotRecover: "You will not be able to restore this without administrator.",
        Page: "Page",
        Show: "Show"
      }
    },
    UA: {
      translations: {
        Login: "Увійти",
        ConfirmPassword: "Підтвердження паролю",
        Signin: "Авторизуватися",
        Comp: "Власник приміщень",
        CComp: "Надавач послуг",
        Name: "Ім'я",
        Email: "Електронна пошта",
        Password: "Пароль",
        Profile: "Головна",
        Type: "Тип",
        WCount: "Кількість вікон",
        Floor: "Поверх",
        Area: "Площа",
        Date:"Дата останнього прибирання",
        Measure: "м²",
        Signup: 'Зареєструватися',
        Rooms: "Приміщення",
        Signout: "Вийти",
        PasswordChangeMsg: 'Залиште поля нижче порожніми, якщо не бажаєте змінювати пароль',
        Actions: "Дії",
        AddR: "Додати приміщення",
        EditR: "Редагувати приміщення",
        EditS:"Редагувати послугу",
        EditP:"Редагувати профіль",
        More:"Більше інформації",
        Edit:"Редагувати",
        Delete:"Видалити",
        Cancel: "Назад",
        Failiture:"Помилка",
        Loading:"Завантаження",
        Save:"Зберегти",
        Backup:"Резервне копіювання даних",
        EConfirmPass: "Паролі не співпадають.",
        Admin:"Адміністратор",
        Phone:"Номер телефону",
        Address:"Адреса",
        City:"м.",
        Street:"вул.",
        House: "буд.",
        FCountry:"Країна",
        FCity:"Місто",
        FStreet:"Вулиця",
        FHouse: "Номер будинку",
        Services:"Послуги прибирання",
        AddS:"Додати послугу",
        Desc:"Опис",
        Price:"Вартість",
        minA:"Мінімальна площа",
        maxA:"Максимальна площа",
        rType:"Тип приміщення",
        pMes:"₴",
        AirQ:"Якість повітря",
        Hum: "Вологість",
        AdjFact:"Коефіцієнт коригування",
        Prior:"Пріоритет",
        Add: "Додати",
        PPM:"Ціна за метр",
        sId:"Ідентифікатор послуги",
        serviceName:"Послуга",
        rId:'Номер приміщення',
        CDate:'Дата',
        Contract:'Договір',
        Contracts:'Договори',
        SContract:"Укласти договір",
        pickSId: "Оберіть послугу",
        pickRId:"Оберіть приміщення",
        selectService: "Послуги",
        selectRoom: "Приміщення",
        selectDate: "Оберіть дату",
        LockUser: "⠀Заблокувати⠀",
        accIsLocked: "Цей обліковий запис заблоковано.",
        UnlockUser: "Розблокувати⠀",
        IsLocked: "Активований",
        BackupWillDownload: "Файл для відновлення даних буде завантажено через декілька секунд...",
        Success: "Добре!",

        DName: "Назва",
        checkCred: "Перевірте правильність введених даних!",
        EName:"Формат вашої назви неправильний!",
        EEmail:"Формат електронної пошти неправильний!",
        EPass:"Формат вашого пароля неправильний!",
        EPhone:"Формат вашого мобільного номера неправильний!",
        ECountry:"Формат вашої країни неправильний!",
        ECity:"Формат вашого міста неправильний!",
        EStreet:"Формат вашої вулиці неправильний!",
        EHouse:"Формат вашого номеру будинку неправильний!",
        EType:"Формат типу неправильний!",
        EFloor:"Формат номеру поверху неправильний!",
        EWCount:"Формат кількості вікон неправильний!",
        EArea:"Формат площі приміщення неправильний!",
        EDesc:"Формат опису неправильний!",
        EEroor:"Возникла помилка.",
        EMinA:"Формат мінімальної площі неправильний!",
        EMaxA:"Формат максимальної площі неправильний!",
        EPPM:"Формат ціни за метр неправильний!",
        NotRecover: "Ви не зможете відновати дані без адміністратора.",
        AreYouSure: "Ви впевнені?",
        Search:"Пошук",
        GlobalPlacementsSearch:"Пошук приміщень...",
        eExist:"Така електронна пошта вже існує в системі!",
        Temp: "Температура",
        dFact: "Рівень забрудненості",
        ConfigureDevice: "Конфігурування IoT",
        SuccessConfiguring: "Пристрій було сконфігуровано",
        DeviceIp: "IP адреса пристрою",
        ServerIp: "Адреса серверу",
        ServerPort: "Порт серверу",
        PlacementId: "Номер приміщення",
        Configure: "Налаштувати",
        Page: "Сторінка",
        Show: "Показати"
      }
    },
  },
  fallbackLng: "en",
  debug: true,

  // have a common namespace used around the full app
  ns: ["translations"],
  defaultNS: "translations",

  keySeparator: false, // we use content as keys

  interpolation: {
    escapeValue: false, // not needed for react!!
    formatSeparator: ","
  },

  react: {
    wait: true
  }
});

export default i18n;
