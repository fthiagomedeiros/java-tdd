movies.sqlite is set to run in the platform below.
https://sqliteonline.com/

File > Open DB

- What are the 3 most popular movies? 

```sql
SELECT original_title, popularity FROM movies
ORDER BY popularity DESC
LIMIT 3;
```

- What is the 3 most awarded average vote since the January 1st, 2000?

```sql
SELECT original_title, vote_average, COUNT(*) AS counter FROM movies
WHERE release_date > '2000-01-01'
GROUP BY vote_average
ORDER BY counter DESC LIMIT 3;

```

- Which movie(s) were directed by Brenda Chapman?

```sql
SELECT * FROM movies m
JOIN directors d ON d.id = m.director_id
WHERE d.name = 'Brenda Chapman'
```

- Which director is the most bankable?

```sql
SELECT d.name, sum(m.revenue) as revenues 
FROM movies m
JOIN directors d ON d.id = m.director_id
GROUP BY d.name
ORDER BY revenues DESC
```



```text
Hints
You can find a great documentation about SQL on
 https://www.sqlservertutorial.net/sql-server-basics/


Explore the many-to-many relation 
https://dzone.com/articles/how-to-handle-a-many-to-many-relationship-in-datab 
and the join tables https://learn.co/lessons/sql-join-tables-readme

Different kind of `JOIN` 
http://www.sql-join.com/sql-join-types

Did you know you can use SQL to insert, 
update and delete records into a database? 
Learn more about the so-called SQL CRUD 
https://www.sqlshack.com/crud-operations-in-sql-server/


You can enhance your SQL skills on DataCamp 
https://www.datacamp.com/courses/tech:sql
```



