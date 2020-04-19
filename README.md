# ExchangeRates
Currency Exchange Rates app for Myanmar, using Central Bank of Myanmar API

> Please note that I'm sharing my projects for free to be able to learn for beginners and intermediate level developers. So if you like my projects, you can buy me a coffee. View all of README.md for more info.

### Central Bank of Myanmar API
1. http://forex.cbm.gov.mm/api/latest
2. http://forex.cbm.gov.mm/api/currencies
3. http://forex.cbm.gov.mm/api/history/29-10-1988(dd-mm-yyyy)

#### 1. Latest API

```json
{
  "info": "Central Bank of Myanmar",
  "description": "Official Website of Central Bank of Myanmar",
  "timestamp": "1337936081",
  "rates": {
    "USD": "840",
    "CHF": "1319",
    "BDT": "298",
    "SGD": "632",
    "JPY": "1053",
    "GBP": "887",
    "AUD": "759"
  }
}
```

#### 2. Currencies API

```json
{
  "info": "Central Bank of Myanmar",
  "description": "Official Website of Central Bank of Myanmar",
  "currencies": {
    "EUR": "Euro",
    "GBP": "Pound sterling",
    "CHF": "Swiss franc",
    "JPY": "Japanese yen",
    "AUD": "Australian dollar",
    "USD": "United State Dollar",
    "INR": "Indian rupee",
    "Peso": "Phillipines Peso"
  }
}
```

#### 3. History API

```json
{
  "info": "Central Bank of Myanmar",
  "description": "Official Website of Central Bank of Myanmar",
  "timestamp": 1341077400,
  "rates": {
    "USD": "882",
    "AUD": "864",
    "GBP": "1288",
    "EUR": "1073",
    "INR": "15",
    "JPY": "983",
    "CHF": "888"
  }
}
```

##### Please note that History API returns `rates` as empty JSONArray if there is no data at your selected date.

#### *Buy Me a Coffee*
- CB Bank : 0065-6001-0005-6898

- KBZ Pay, Wave Pay : 09689966123
- Visa : 4279-4700-0022-2100


