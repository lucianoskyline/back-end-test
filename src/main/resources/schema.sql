create table category(
  id integer not null,
  name varchar(80),
  primary key(id)
);

create table product(
  id integer not null,
  name varchar(80),
  category integer,
  primary key(id)
);

alter table product add foreign key(category) references category(id);