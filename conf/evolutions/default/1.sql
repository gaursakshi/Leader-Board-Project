# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table player_scores (
  player_id                     varchar(255) not null,
  score                         bigint,
  player_name                   varchar(255),
  constraint pk_player_scores primary key (player_id)
);


# --- !Downs

drop table if exists player_scores cascade;

