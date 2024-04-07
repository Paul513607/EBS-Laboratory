# Tests

We test the time taken to generate subscriptions and publications.
The processor specifications on which the tests were run:

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
  "numberOfPublications": 10000000,
  "numberOfSubscriptions": 10000000
}
```

Time taken: 6.4078025s (seconds).

## Test 4

Configuration:

```json
{
  "numberOfThreads": 4,
  "numberOfPublications": 1000000,
  "numberOfSubscriptions": 1000000
}
```

Time taken: 4.3553943s (seconds).

## Test 5

Configuration:

```json
{
  "numberOfThreads": 8,
  "numberOfPublications": 1000000,
  "numberOfSubscriptions": 1000000
}
```

Time taken: 4.144836s (seconds).
