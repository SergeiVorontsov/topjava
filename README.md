[![Codacy Badge](https://app.codacy.com/project/badge/Grade/766d40ee05ba4598b446abbbe64fb4a3)](https://www.codacy.com/gh/SergeiVorontsov/topjava/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=SergeiVorontsov/topjava&amp;utm_campaign=Badge_Grade)

## Rest API

### commands

#### get

`curl --location 'http://localhost:8090/topjava/rest/meals/100003'`

#### delete

`curl --location --request DELETE 'http://localhost:8090/topjava/rest/meals/100003'`

#### getAll

`curl --location 'http://localhost:8090/topjava/rest/meals'`

#### createWithLocation

`curl --location 'http://localhost:8090/topjava/rest/meals' \
--header 'Content-Type: application/json' \
--data '{
"dateTime":"2020-02-01T18:00:00",
"description":"Созданный ужин",
"calories":300
}'`

#### update

`curl --location --request PUT 'http://localhost:8090/topjava/rest/meals/100003' \
--header 'Content-Type: application/json' \
--data '{"id":100003,"dateTime":"2020-01-30T10:02:00","description":"Обновленный завтрак","calories":200}'`

#### filter

`curl --location --request GET 'http://localhost:8090/topjava/rest/meals/filter?startDate=2020-01-30&startTime=10%3A00&endDate=2020-01-30&endTime=23%3A59' \
--header 'Content-Type: application/json' \
--data '{startDate=[2020-01-30], startTime=[10:00], endDate=[2020-01-30], endTime=[23:59]}'`