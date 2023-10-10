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


