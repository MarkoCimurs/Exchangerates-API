CREATE TABLE currencies(
    id bigint auto_increment primary key,
    code varchar(3) not null unique
);

CREATE TABLE dates(
    id bigint auto_increment primary key,
    date datetime not null unique
);

CREATE TABLE exchangeRateFromEuro(
    currency_id bigint not null,
    date_id bigint not null,
    valueRate float not null,
    primary key(currency_id, date_id),
    foreign key (currency_id) references  currencies(id) on delete cascade,
    foreign key (date_id) references dates(id) on delete cascade
);