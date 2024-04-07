# Tests

We test the time taken to generate subscriptions and publications.
The processor specifications on which the tests were run:
12th Gen Intel(R) Core(TM) i7-12700 2.10 GHz

## Test 1

Configuration:

```json
{
  "companyPossibleValues": ["C1", "C2", "C3"],
  "valueMin": 0.0,
  "valueMax": 100.0,
  "dropMin": 0.0,
  "dropMax": 50.0,
  "variationMin": 0.0,
  "variationMax": 1.0,
  "datePossibleValues": ["2024-04-06", "2024-04-07", "2024-04-08"],

  "companyFrequency": 0.7,
  "valueFrequency": 0.5,
  "dropFrequency": 0.3,
  "variationFrequency": 0.2,
  "dateFrequency": 0.5,

  "companyOperatorFrequency": 0.5,
  "valueOperatorFrequency": 0.5,
  "dropOperatorFrequency": 0.5,
  "variationOperatorFrequency": 0.5,
  "dateOperatorFrequency": 0.5,

  "numberOfThreads": 1,
  "numberOfPublications": 1000,
  "numberOfSubscriptions": 1000
}
```

Time taken: 0.0106926s (seconds).

## Test 2

Configuration:

```json
{
  "numberOfThreads": 4,
  "numberOfPublications": 1000,
  "numberOfSubscriptions": 1000
}
```

Time taken: 0.0107621s (seconds).

## Test 3

Configuration:

```json
{
  "numberOfThreads": 1,
  "numberOfPublications": 1000000,
  "numberOfSubscriptions": 1000000
}
```

Time taken: 0.6536817s (seconds).

## Test 4

Configuration:

```json
{
  "numberOfThreads": 4,
  "numberOfPublications": 1000000,
  "numberOfSubscriptions": 1000000
}
```

Time taken: 0.5443151s (seconds).

## Test 5

Configuration:

```json
{
  "numberOfThreads": 4,
  "numberOfPublications": 10000000,
  "numberOfSubscriptions": 10000000
}
```

Time taken: 4.9677217s (seconds).

## Test 6

Configuration:

```json
{
  "numberOfThreads": 8,
  "numberOfPublications": 10000000,
  "numberOfSubscriptions": 10000000
}
```

Time taken: 4.2959808s (seconds).

## Test 7

Configuration:

```json
{
  "numberOfThreads": 16,
  "numberOfPublications": 10000000,
  "numberOfSubscriptions": 10000000
}
```

Time taken: 3.89905s (seconds).

## Test 8

Configuration:

```json
{
  "companyPossibleValues": ["C1", "C2", "C3"],
  "valueMin": 0.0,
  "valueMax": 100.0,
  "dropMin": 0.0,
  "dropMax": 50.0,
  "variationMin": 0.0,
  "variationMax": 1.0,
  "datePossibleValues": ["2024-04-06", "2024-04-07", "2024-04-08"],

  "companyFrequency": 1,
  "valueFrequency": 1,
  "dropFrequency": 1,
  "variationFrequency": 1,
  "dateFrequency": 1,

  "companyOperatorFrequency": 0.5,
  "valueOperatorFrequency": 0.5,
  "dropOperatorFrequency": 0.5,
  "variationOperatorFrequency": 0.5,
  "dateOperatorFrequency": 0.5,

  "numberOfThreads": 20,
  "numberOfPublications": 10,
  "numberOfSubscriptions": 10
}
```

Time taken: 0.0070651s (seconds).
Result:

```json
{(drop,>,48.73);(date,=,08.04.2024);(company,=,"C1");(value,=,13.72);(variation,>,0.88)}
{(drop,=,41.83);(date,<,07.04.2024);(company,=,"C2");(value,=,69.2);(variation,<,0.32)}
{(drop,<,19.52);(date,=,06.04.2024);(company,=,"C3");(value,=,63.2);(variation,=,0.55)}
{(drop,<=,18.24);(date,=,06.04.2024);(company,<=,"C3");(value,=,19.95);(variation,=,0.65)}
{(drop,<,6.41);(date,=,08.04.2024);(company,<,"C1");(value,=,46.04);(variation,=,0.54)}
{(drop,=,20.1);(date,=,07.04.2024);(company,=,"C1");(value,>=,0.6);(variation,>=,0.55)}
{(drop,=,11.41);(date,=,08.04.2024);(company,>=,"C2");(value,<,70.17);(variation,=,0.88)}
{(drop,=,12.95);(date,>,07.04.2024);(company,>=,"C2");(value,=,0.19);(variation,=,0.62)}
{(drop,<=,40.04);(date,=,08.04.2024);(company,=,"C3");(value,>=,92.69);(variation,>,0.74)}
{(drop,=,6.08);(date,=,06.04.2024);(company,>,"C1");(value,=,86.35);(variation,<,0.51)}
```
