# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table match_score (
  id                            bigserial not null,
  player_one_id                 bigint not null,
  player_two_id                 bigint,
  player_one_points             integer,
  player_two_points             integer,
  player_one_rank_delta         integer,
  player_two_rank_delta         integer,
  player_one_score_delta        bigint,
  player_two_score_delta        bigint,
  creation_ts                   timestamp,
  constraint pk_match_score primary key (id)
);

create table player (
  id                            bigserial not null,
  name                          varchar(255),
  position                      integer,
  latest_position_delta         integer,
  ranking                       bigint,
  matches_played                integer,
  win_count                     integer,
  lost_count                    integer,
  constraint pk_player primary key (id)
);

alter table match_score add constraint fk_match_score_player_one_id foreign key (player_one_id) references player (id) on delete restrict on update restrict;
create index ix_match_score_player_one_id on match_score (player_one_id);

alter table match_score add constraint fk_match_score_player_two_id foreign key (player_two_id) references player (id) on delete restrict on update restrict;
create index ix_match_score_player_two_id on match_score (player_two_id);


# --- !Downs

alter table if exists match_score drop constraint if exists fk_match_score_player_one_id;
drop index if exists ix_match_score_player_one_id;

alter table if exists match_score drop constraint if exists fk_match_score_player_two_id;
drop index if exists ix_match_score_player_two_id;

drop table if exists match_score cascade;

drop table if exists player cascade;

