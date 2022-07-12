
    create table banned (
       ban_id bigint not null auto_increment,
        timestamp date,
        banned_by_user_id bigint,
        banned_user_user_id bigint,
        for_community_community_id bigint,
        primary key (ban_id)
    ) engine=InnoDB;

    create table comment (
       comment_id bigint not null auto_increment,
        is_deleted bit not null,
        text varchar(255),
        timestamp date not null,
        belongs_to_post_post_id bigint,
        belongs_to_user_user_id bigint,
        parent_comment_comment_id bigint,
        primary key (comment_id)
    ) engine=InnoDB;

    create table community (
       community_id bigint not null auto_increment,
        creation_date date not null,
        description varchar(255),
        is_suspended bit not null,
        name varchar(255) not null,
        suspension_reason varchar(255),
        primary key (community_id)
    ) engine=InnoDB;

    create table community_flairs (
       community_community_id bigint not null,
        flairs_flair_id bigint not null,
        primary key (community_community_id, flairs_flair_id)
    ) engine=InnoDB;

    create table flair (
       flair_id bigint not null auto_increment,
        name varchar(255),
        primary key (flair_id)
    ) engine=InnoDB;

    create table flair_communities (
       flair_flair_id bigint not null,
        communities_community_id bigint not null,
        primary key (flair_flair_id, communities_community_id)
    ) engine=InnoDB;

    create table moderator (
       u_id bigint not null,
        commu_id bigint not null,
        primary key (u_id, commu_id)
    ) engine=InnoDB;

    create table post (
       post_id bigint not null auto_increment,
        creation_date date not null,
        image_path varchar(255),
        text varchar(255),
        title varchar(255) not null,
        community_id bigint,
        flair_id bigint,
        user_id bigint,
        primary key (post_id)
    ) engine=InnoDB;

    create table reaction (
       reaction_id bigint not null auto_increment,
        timestamp date,
        type varchar(255) not null,
        made_by_user_id bigint,
        to_comment_comment_id bigint,
        to_post_post_id bigint,
        primary key (reaction_id)
    ) engine=InnoDB;

    create table report (
       report_id bigint not null auto_increment,
        accepted bit,
        reason varchar(255) not null,
        timestamp date,
        by_user_user_id bigint,
        for_comment_comment_id bigint,
        for_post_post_id bigint,
        primary key (report_id)
    ) engine=InnoDB;

    create table rule (
       rule_id bigint not null auto_increment,
        belongs_to_community_community_id bigint,
        primary key (rule_id)
    ) engine=InnoDB;

    create table user (
       role varchar(31) not null,
        user_id bigint not null auto_increment,
        avatar varchar(255),
        description varchar(255),
        display_name varchar(255),
        email varchar(255),
        password varchar(255) not null,
        registration_date date not null,
        username varchar(255) not null,
        primary key (user_id)
    ) engine=InnoDB;

    alter table user 
       add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);

    alter table banned 
       add constraint FKa836rewdg8n0a2vrayrkqb60l 
       foreign key (banned_by_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK5wswre9sf1uu41h2ifkhrfjqw 
       foreign key (banned_user_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK574c1sm21w3h7m90gu8k8jd5n 
       foreign key (for_community_community_id) 
       references community (community_id);

    alter table comment 
       add constraint FK4pwsbq31a29r5fds2nqvtopwg 
       foreign key (belongs_to_post_post_id) 
       references post (post_id);

    alter table comment 
       add constraint FK8mojd6ch4drippcsb6pk2mp9v 
       foreign key (belongs_to_user_user_id) 
       references user (user_id);

    alter table comment 
       add constraint FKc43l7bomr9sf0si605of2aakc 
       foreign key (parent_comment_comment_id) 
       references comment (comment_id);

    alter table community_flairs 
       add constraint FKpasiuv71rm8p4fr88busfcnqk 
       foreign key (flairs_flair_id) 
       references flair (flair_id);

    alter table community_flairs 
       add constraint FK9llkab37jr25pcw4v8fulslgj 
       foreign key (community_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKkdtoj42e43tf36qf359iebf8u 
       foreign key (communities_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKhwf3p8i50edrttqtmca1goab3 
       foreign key (flair_flair_id) 
       references flair (flair_id);

    alter table moderator 
       add constraint FK3jw301te4vawnv0nnjqbkoclx 
       foreign key (commu_id) 
       references community (community_id);

    alter table moderator 
       add constraint FK6viwndd99apby757x1bk2jfrt 
       foreign key (u_id) 
       references user (user_id);

    alter table post 
       add constraint FKokm06ignilxux2n1anwepgun7 
       foreign key (community_id) 
       references community (community_id);

    alter table post 
       add constraint FK6m84rx2dwjhng5qvfhq0rqadd 
       foreign key (flair_id) 
       references flair (flair_id);

    alter table post 
       add constraint FK72mt33dhhs48hf9gcqrq4fxte 
       foreign key (user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FKhs2tfxmmbtn5e5yfn537p2h5j 
       foreign key (made_by_user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FK8br49vcjbugdpwedd0bknwwtw 
       foreign key (to_comment_comment_id) 
       references comment (comment_id);

    alter table reaction 
       add constraint FKso87s37i14ldnmj9latpmd491 
       foreign key (to_post_post_id) 
       references post (post_id);

    alter table report 
       add constraint FKb6bt0hd02jy69jc52pn14creg 
       foreign key (by_user_user_id) 
       references user (user_id);

    alter table report 
       add constraint FKi0cmoo16sg6efbnnyfwcgm8qg 
       foreign key (for_comment_comment_id) 
       references comment (comment_id);

    alter table report 
       add constraint FKri5qf0b11l06wc2pcq0fj1l82 
       foreign key (for_post_post_id) 
       references post (post_id);

    alter table rule 
       add constraint FK8yk9btibk8xau3awcf39dms18 
       foreign key (belongs_to_community_community_id) 
       references community (community_id);

    create table banned (
       ban_id bigint not null auto_increment,
        timestamp date,
        banned_by_user_id bigint,
        banned_user_user_id bigint,
        for_community_community_id bigint,
        primary key (ban_id)
    ) engine=InnoDB;

    create table comment (
       comment_id bigint not null auto_increment,
        is_deleted bit not null,
        text varchar(255),
        timestamp date not null,
        belongs_to_post_post_id bigint,
        belongs_to_user_user_id bigint,
        parent_comment_comment_id bigint,
        primary key (comment_id)
    ) engine=InnoDB;

    create table community (
       community_id bigint not null auto_increment,
        creation_date date not null,
        description varchar(255),
        is_suspended bit not null,
        name varchar(255) not null,
        suspension_reason varchar(255),
        primary key (community_id)
    ) engine=InnoDB;

    create table community_flairs (
       community_community_id bigint not null,
        flairs_flair_id bigint not null,
        primary key (community_community_id, flairs_flair_id)
    ) engine=InnoDB;

    create table flair (
       flair_id bigint not null auto_increment,
        name varchar(255),
        primary key (flair_id)
    ) engine=InnoDB;

    create table flair_communities (
       flair_flair_id bigint not null,
        communities_community_id bigint not null,
        primary key (flair_flair_id, communities_community_id)
    ) engine=InnoDB;

    create table moderator (
       u_id bigint not null,
        commu_id bigint not null,
        primary key (u_id, commu_id)
    ) engine=InnoDB;

    create table post (
       post_id bigint not null auto_increment,
        creation_date date not null,
        image_path varchar(255),
        text varchar(255),
        title varchar(255) not null,
        community_id bigint,
        flair_id bigint,
        user_id bigint,
        primary key (post_id)
    ) engine=InnoDB;

    create table reaction (
       reaction_id bigint not null auto_increment,
        timestamp date,
        type varchar(255) not null,
        made_by_user_id bigint,
        to_comment_comment_id bigint,
        to_post_post_id bigint,
        primary key (reaction_id)
    ) engine=InnoDB;

    create table report (
       report_id bigint not null auto_increment,
        accepted bit,
        reason varchar(255) not null,
        timestamp date,
        by_user_user_id bigint,
        for_comment_comment_id bigint,
        for_post_post_id bigint,
        primary key (report_id)
    ) engine=InnoDB;

    create table rule (
       rule_id bigint not null auto_increment,
        belongs_to_community_community_id bigint,
        primary key (rule_id)
    ) engine=InnoDB;

    create table user (
       role varchar(31) not null,
        user_id bigint not null auto_increment,
        avatar varchar(255),
        description varchar(255),
        display_name varchar(255),
        email varchar(255),
        password varchar(255) not null,
        registration_date date not null,
        username varchar(255) not null,
        primary key (user_id)
    ) engine=InnoDB;

    alter table user 
       add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);

    alter table banned 
       add constraint FKa836rewdg8n0a2vrayrkqb60l 
       foreign key (banned_by_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK5wswre9sf1uu41h2ifkhrfjqw 
       foreign key (banned_user_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK574c1sm21w3h7m90gu8k8jd5n 
       foreign key (for_community_community_id) 
       references community (community_id);

    alter table comment 
       add constraint FK4pwsbq31a29r5fds2nqvtopwg 
       foreign key (belongs_to_post_post_id) 
       references post (post_id);

    alter table comment 
       add constraint FK8mojd6ch4drippcsb6pk2mp9v 
       foreign key (belongs_to_user_user_id) 
       references user (user_id);

    alter table comment 
       add constraint FKc43l7bomr9sf0si605of2aakc 
       foreign key (parent_comment_comment_id) 
       references comment (comment_id);

    alter table community_flairs 
       add constraint FKpasiuv71rm8p4fr88busfcnqk 
       foreign key (flairs_flair_id) 
       references flair (flair_id);

    alter table community_flairs 
       add constraint FK9llkab37jr25pcw4v8fulslgj 
       foreign key (community_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKkdtoj42e43tf36qf359iebf8u 
       foreign key (communities_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKhwf3p8i50edrttqtmca1goab3 
       foreign key (flair_flair_id) 
       references flair (flair_id);

    alter table moderator 
       add constraint FK3jw301te4vawnv0nnjqbkoclx 
       foreign key (commu_id) 
       references community (community_id);

    alter table moderator 
       add constraint FK6viwndd99apby757x1bk2jfrt 
       foreign key (u_id) 
       references user (user_id);

    alter table post 
       add constraint FKokm06ignilxux2n1anwepgun7 
       foreign key (community_id) 
       references community (community_id);

    alter table post 
       add constraint FK6m84rx2dwjhng5qvfhq0rqadd 
       foreign key (flair_id) 
       references flair (flair_id);

    alter table post 
       add constraint FK72mt33dhhs48hf9gcqrq4fxte 
       foreign key (user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FKhs2tfxmmbtn5e5yfn537p2h5j 
       foreign key (made_by_user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FK8br49vcjbugdpwedd0bknwwtw 
       foreign key (to_comment_comment_id) 
       references comment (comment_id);

    alter table reaction 
       add constraint FKso87s37i14ldnmj9latpmd491 
       foreign key (to_post_post_id) 
       references post (post_id);

    alter table report 
       add constraint FKb6bt0hd02jy69jc52pn14creg 
       foreign key (by_user_user_id) 
       references user (user_id);

    alter table report 
       add constraint FKi0cmoo16sg6efbnnyfwcgm8qg 
       foreign key (for_comment_comment_id) 
       references comment (comment_id);

    alter table report 
       add constraint FKri5qf0b11l06wc2pcq0fj1l82 
       foreign key (for_post_post_id) 
       references post (post_id);

    alter table rule 
       add constraint FK8yk9btibk8xau3awcf39dms18 
       foreign key (belongs_to_community_community_id) 
       references community (community_id);

    create table banned (
       ban_id bigint not null auto_increment,
        timestamp date,
        banned_by_user_id bigint,
        banned_user_user_id bigint,
        for_community_community_id bigint,
        primary key (ban_id)
    ) engine=InnoDB;

    create table comment (
       comment_id bigint not null auto_increment,
        is_deleted bit not null,
        text varchar(255),
        timestamp date not null,
        belongs_to_post_post_id bigint,
        belongs_to_user_user_id bigint,
        parent_comment_comment_id bigint,
        primary key (comment_id)
    ) engine=InnoDB;

    create table community (
       community_id bigint not null auto_increment,
        creation_date date not null,
        description varchar(255),
        is_suspended bit not null,
        name varchar(255) not null,
        suspension_reason varchar(255),
        primary key (community_id)
    ) engine=InnoDB;

    create table community_flairs (
       community_community_id bigint not null,
        flairs_flair_id bigint not null,
        primary key (community_community_id, flairs_flair_id)
    ) engine=InnoDB;

    create table flair (
       flair_id bigint not null auto_increment,
        name varchar(255),
        primary key (flair_id)
    ) engine=InnoDB;

    create table flair_communities (
       flair_flair_id bigint not null,
        communities_community_id bigint not null,
        primary key (flair_flair_id, communities_community_id)
    ) engine=InnoDB;

    create table moderator (
       u_id bigint not null,
        commu_id bigint not null,
        primary key (u_id, commu_id)
    ) engine=InnoDB;

    create table post (
       post_id bigint not null auto_increment,
        creation_date date not null,
        image_path varchar(255),
        text varchar(255),
        title varchar(255) not null,
        community_id bigint,
        flair_id bigint,
        user_id bigint,
        primary key (post_id)
    ) engine=InnoDB;

    create table reaction (
       reaction_id bigint not null auto_increment,
        timestamp date,
        type varchar(255) not null,
        made_by_user_id bigint,
        to_comment_comment_id bigint,
        to_post_post_id bigint,
        primary key (reaction_id)
    ) engine=InnoDB;

    create table report (
       report_id bigint not null auto_increment,
        accepted bit,
        reason varchar(255) not null,
        timestamp date,
        by_user_user_id bigint,
        for_comment_comment_id bigint,
        for_post_post_id bigint,
        primary key (report_id)
    ) engine=InnoDB;

    create table rule (
       rule_id bigint not null auto_increment,
        belongs_to_community_community_id bigint,
        primary key (rule_id)
    ) engine=InnoDB;

    create table user (
       role varchar(31) not null,
        user_id bigint not null auto_increment,
        avatar varchar(255),
        description varchar(255),
        display_name varchar(255),
        email varchar(255),
        password varchar(255) not null,
        registration_date date not null,
        username varchar(255) not null,
        primary key (user_id)
    ) engine=InnoDB;

    alter table user 
       add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);

    alter table banned 
       add constraint FKa836rewdg8n0a2vrayrkqb60l 
       foreign key (banned_by_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK5wswre9sf1uu41h2ifkhrfjqw 
       foreign key (banned_user_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK574c1sm21w3h7m90gu8k8jd5n 
       foreign key (for_community_community_id) 
       references community (community_id);

    alter table comment 
       add constraint FK4pwsbq31a29r5fds2nqvtopwg 
       foreign key (belongs_to_post_post_id) 
       references post (post_id);

    alter table comment 
       add constraint FK8mojd6ch4drippcsb6pk2mp9v 
       foreign key (belongs_to_user_user_id) 
       references user (user_id);

    alter table comment 
       add constraint FKc43l7bomr9sf0si605of2aakc 
       foreign key (parent_comment_comment_id) 
       references comment (comment_id);

    alter table community_flairs 
       add constraint FKpasiuv71rm8p4fr88busfcnqk 
       foreign key (flairs_flair_id) 
       references flair (flair_id);

    alter table community_flairs 
       add constraint FK9llkab37jr25pcw4v8fulslgj 
       foreign key (community_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKkdtoj42e43tf36qf359iebf8u 
       foreign key (communities_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKhwf3p8i50edrttqtmca1goab3 
       foreign key (flair_flair_id) 
       references flair (flair_id);

    alter table moderator 
       add constraint FK3jw301te4vawnv0nnjqbkoclx 
       foreign key (commu_id) 
       references community (community_id);

    alter table moderator 
       add constraint FK6viwndd99apby757x1bk2jfrt 
       foreign key (u_id) 
       references user (user_id);

    alter table post 
       add constraint FKokm06ignilxux2n1anwepgun7 
       foreign key (community_id) 
       references community (community_id);

    alter table post 
       add constraint FK6m84rx2dwjhng5qvfhq0rqadd 
       foreign key (flair_id) 
       references flair (flair_id);

    alter table post 
       add constraint FK72mt33dhhs48hf9gcqrq4fxte 
       foreign key (user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FKhs2tfxmmbtn5e5yfn537p2h5j 
       foreign key (made_by_user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FK8br49vcjbugdpwedd0bknwwtw 
       foreign key (to_comment_comment_id) 
       references comment (comment_id);

    alter table reaction 
       add constraint FKso87s37i14ldnmj9latpmd491 
       foreign key (to_post_post_id) 
       references post (post_id);

    alter table report 
       add constraint FKb6bt0hd02jy69jc52pn14creg 
       foreign key (by_user_user_id) 
       references user (user_id);

    alter table report 
       add constraint FKi0cmoo16sg6efbnnyfwcgm8qg 
       foreign key (for_comment_comment_id) 
       references comment (comment_id);

    alter table report 
       add constraint FKri5qf0b11l06wc2pcq0fj1l82 
       foreign key (for_post_post_id) 
       references post (post_id);

    alter table rule 
       add constraint FK8yk9btibk8xau3awcf39dms18 
       foreign key (belongs_to_community_community_id) 
       references community (community_id);

    create table banned (
       ban_id bigint not null auto_increment,
        timestamp date,
        banned_by_user_id bigint,
        banned_user_user_id bigint,
        for_community_community_id bigint,
        primary key (ban_id)
    ) engine=InnoDB;

    create table comment (
       comment_id bigint not null auto_increment,
        is_deleted bit not null,
        text varchar(255),
        timestamp date not null,
        belongs_to_post_post_id bigint,
        belongs_to_user_user_id bigint,
        parent_comment_comment_id bigint,
        primary key (comment_id)
    ) engine=InnoDB;

    create table community (
       community_id bigint not null auto_increment,
        creation_date date not null,
        description varchar(255),
        is_suspended bit not null,
        name varchar(255) not null,
        suspension_reason varchar(255),
        primary key (community_id)
    ) engine=InnoDB;

    create table community_flairs (
       community_community_id bigint not null,
        flairs_flair_id bigint not null,
        primary key (community_community_id, flairs_flair_id)
    ) engine=InnoDB;

    create table flair (
       flair_id bigint not null auto_increment,
        name varchar(255),
        primary key (flair_id)
    ) engine=InnoDB;

    create table flair_communities (
       flair_flair_id bigint not null,
        communities_community_id bigint not null,
        primary key (flair_flair_id, communities_community_id)
    ) engine=InnoDB;

    create table moderator (
       u_id bigint not null,
        commu_id bigint not null,
        primary key (u_id, commu_id)
    ) engine=InnoDB;

    create table post (
       post_id bigint not null auto_increment,
        creation_date date not null,
        image_path varchar(255),
        text varchar(255),
        title varchar(255) not null,
        community_id bigint,
        flair_id bigint,
        user_id bigint,
        primary key (post_id)
    ) engine=InnoDB;

    create table reaction (
       reaction_id bigint not null auto_increment,
        timestamp date,
        type varchar(255) not null,
        made_by_user_id bigint,
        to_comment_comment_id bigint,
        to_post_post_id bigint,
        primary key (reaction_id)
    ) engine=InnoDB;

    create table report (
       report_id bigint not null auto_increment,
        accepted bit,
        reason varchar(255) not null,
        timestamp date,
        by_user_user_id bigint,
        for_comment_comment_id bigint,
        for_post_post_id bigint,
        primary key (report_id)
    ) engine=InnoDB;

    create table rule (
       rule_id bigint not null auto_increment,
        belongs_to_community_community_id bigint,
        primary key (rule_id)
    ) engine=InnoDB;

    create table user (
       role varchar(31) not null,
        user_id bigint not null auto_increment,
        avatar varchar(255),
        description varchar(255),
        display_name varchar(255),
        email varchar(255),
        password varchar(255) not null,
        registration_date date not null,
        username varchar(255) not null,
        primary key (user_id)
    ) engine=InnoDB;

    alter table user 
       add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);

    alter table banned 
       add constraint FKa836rewdg8n0a2vrayrkqb60l 
       foreign key (banned_by_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK5wswre9sf1uu41h2ifkhrfjqw 
       foreign key (banned_user_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK574c1sm21w3h7m90gu8k8jd5n 
       foreign key (for_community_community_id) 
       references community (community_id);

    alter table comment 
       add constraint FK4pwsbq31a29r5fds2nqvtopwg 
       foreign key (belongs_to_post_post_id) 
       references post (post_id);

    alter table comment 
       add constraint FK8mojd6ch4drippcsb6pk2mp9v 
       foreign key (belongs_to_user_user_id) 
       references user (user_id);

    alter table comment 
       add constraint FKc43l7bomr9sf0si605of2aakc 
       foreign key (parent_comment_comment_id) 
       references comment (comment_id);

    alter table community_flairs 
       add constraint FKpasiuv71rm8p4fr88busfcnqk 
       foreign key (flairs_flair_id) 
       references flair (flair_id);

    alter table community_flairs 
       add constraint FK9llkab37jr25pcw4v8fulslgj 
       foreign key (community_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKkdtoj42e43tf36qf359iebf8u 
       foreign key (communities_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKhwf3p8i50edrttqtmca1goab3 
       foreign key (flair_flair_id) 
       references flair (flair_id);

    alter table moderator 
       add constraint FK3jw301te4vawnv0nnjqbkoclx 
       foreign key (commu_id) 
       references community (community_id);

    alter table moderator 
       add constraint FK6viwndd99apby757x1bk2jfrt 
       foreign key (u_id) 
       references user (user_id);

    alter table post 
       add constraint FKokm06ignilxux2n1anwepgun7 
       foreign key (community_id) 
       references community (community_id);

    alter table post 
       add constraint FK6m84rx2dwjhng5qvfhq0rqadd 
       foreign key (flair_id) 
       references flair (flair_id);

    alter table post 
       add constraint FK72mt33dhhs48hf9gcqrq4fxte 
       foreign key (user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FKhs2tfxmmbtn5e5yfn537p2h5j 
       foreign key (made_by_user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FK8br49vcjbugdpwedd0bknwwtw 
       foreign key (to_comment_comment_id) 
       references comment (comment_id);

    alter table reaction 
       add constraint FKso87s37i14ldnmj9latpmd491 
       foreign key (to_post_post_id) 
       references post (post_id);

    alter table report 
       add constraint FKb6bt0hd02jy69jc52pn14creg 
       foreign key (by_user_user_id) 
       references user (user_id);

    alter table report 
       add constraint FKi0cmoo16sg6efbnnyfwcgm8qg 
       foreign key (for_comment_comment_id) 
       references comment (comment_id);

    alter table report 
       add constraint FKri5qf0b11l06wc2pcq0fj1l82 
       foreign key (for_post_post_id) 
       references post (post_id);

    alter table rule 
       add constraint FK8yk9btibk8xau3awcf39dms18 
       foreign key (belongs_to_community_community_id) 
       references community (community_id);

    create table banned (
       ban_id bigint not null auto_increment,
        timestamp date,
        banned_by_user_id bigint,
        banned_user_user_id bigint,
        for_community_community_id bigint,
        primary key (ban_id)
    ) engine=InnoDB;

    create table comment (
       comment_id bigint not null auto_increment,
        is_deleted bit not null,
        text varchar(255),
        timestamp date not null,
        belongs_to_post_post_id bigint,
        belongs_to_user_user_id bigint,
        parent_comment_comment_id bigint,
        primary key (comment_id)
    ) engine=InnoDB;

    create table community (
       community_id bigint not null auto_increment,
        creation_date date not null,
        description varchar(255),
        is_suspended bit not null,
        name varchar(255) not null,
        suspension_reason varchar(255),
        primary key (community_id)
    ) engine=InnoDB;

    create table community_flairs (
       community_community_id bigint not null,
        flairs_flair_id bigint not null,
        primary key (community_community_id, flairs_flair_id)
    ) engine=InnoDB;

    create table flair (
       flair_id bigint not null auto_increment,
        name varchar(255),
        primary key (flair_id)
    ) engine=InnoDB;

    create table flair_communities (
       flair_flair_id bigint not null,
        communities_community_id bigint not null,
        primary key (flair_flair_id, communities_community_id)
    ) engine=InnoDB;

    create table moderator (
       u_id bigint not null,
        commu_id bigint not null,
        primary key (u_id, commu_id)
    ) engine=InnoDB;

    create table post (
       post_id bigint not null auto_increment,
        creation_date date not null,
        image_path varchar(255),
        text varchar(255),
        title varchar(255) not null,
        community_id bigint,
        flair_id bigint,
        user_id bigint,
        primary key (post_id)
    ) engine=InnoDB;

    create table reaction (
       reaction_id bigint not null auto_increment,
        timestamp date,
        type varchar(255) not null,
        made_by_user_id bigint,
        to_comment_comment_id bigint,
        to_post_post_id bigint,
        primary key (reaction_id)
    ) engine=InnoDB;

    create table report (
       report_id bigint not null auto_increment,
        accepted bit,
        reason varchar(255) not null,
        timestamp date,
        by_user_user_id bigint,
        for_comment_comment_id bigint,
        for_post_post_id bigint,
        primary key (report_id)
    ) engine=InnoDB;

    create table rule (
       rule_id bigint not null auto_increment,
        belongs_to_community_community_id bigint,
        primary key (rule_id)
    ) engine=InnoDB;

    create table user (
       role varchar(31) not null,
        user_id bigint not null auto_increment,
        avatar varchar(255),
        description varchar(255),
        display_name varchar(255),
        email varchar(255),
        password varchar(255) not null,
        registration_date date not null,
        username varchar(255) not null,
        primary key (user_id)
    ) engine=InnoDB;

    alter table user 
       add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);

    alter table banned 
       add constraint FKa836rewdg8n0a2vrayrkqb60l 
       foreign key (banned_by_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK5wswre9sf1uu41h2ifkhrfjqw 
       foreign key (banned_user_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK574c1sm21w3h7m90gu8k8jd5n 
       foreign key (for_community_community_id) 
       references community (community_id);

    alter table comment 
       add constraint FK4pwsbq31a29r5fds2nqvtopwg 
       foreign key (belongs_to_post_post_id) 
       references post (post_id);

    alter table comment 
       add constraint FK8mojd6ch4drippcsb6pk2mp9v 
       foreign key (belongs_to_user_user_id) 
       references user (user_id);

    alter table comment 
       add constraint FKc43l7bomr9sf0si605of2aakc 
       foreign key (parent_comment_comment_id) 
       references comment (comment_id);

    alter table community_flairs 
       add constraint FKpasiuv71rm8p4fr88busfcnqk 
       foreign key (flairs_flair_id) 
       references flair (flair_id);

    alter table community_flairs 
       add constraint FK9llkab37jr25pcw4v8fulslgj 
       foreign key (community_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKkdtoj42e43tf36qf359iebf8u 
       foreign key (communities_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKhwf3p8i50edrttqtmca1goab3 
       foreign key (flair_flair_id) 
       references flair (flair_id);

    alter table moderator 
       add constraint FK3jw301te4vawnv0nnjqbkoclx 
       foreign key (commu_id) 
       references community (community_id);

    alter table moderator 
       add constraint FK6viwndd99apby757x1bk2jfrt 
       foreign key (u_id) 
       references user (user_id);

    alter table post 
       add constraint FKokm06ignilxux2n1anwepgun7 
       foreign key (community_id) 
       references community (community_id);

    alter table post 
       add constraint FK6m84rx2dwjhng5qvfhq0rqadd 
       foreign key (flair_id) 
       references flair (flair_id);

    alter table post 
       add constraint FK72mt33dhhs48hf9gcqrq4fxte 
       foreign key (user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FKhs2tfxmmbtn5e5yfn537p2h5j 
       foreign key (made_by_user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FK8br49vcjbugdpwedd0bknwwtw 
       foreign key (to_comment_comment_id) 
       references comment (comment_id);

    alter table reaction 
       add constraint FKso87s37i14ldnmj9latpmd491 
       foreign key (to_post_post_id) 
       references post (post_id);

    alter table report 
       add constraint FKb6bt0hd02jy69jc52pn14creg 
       foreign key (by_user_user_id) 
       references user (user_id);

    alter table report 
       add constraint FKi0cmoo16sg6efbnnyfwcgm8qg 
       foreign key (for_comment_comment_id) 
       references comment (comment_id);

    alter table report 
       add constraint FKri5qf0b11l06wc2pcq0fj1l82 
       foreign key (for_post_post_id) 
       references post (post_id);

    alter table rule 
       add constraint FK8yk9btibk8xau3awcf39dms18 
       foreign key (belongs_to_community_community_id) 
       references community (community_id);

    create table banned (
       ban_id bigint not null auto_increment,
        timestamp date,
        banned_by_user_id bigint,
        banned_user_user_id bigint,
        for_community_community_id bigint,
        primary key (ban_id)
    ) engine=InnoDB;

    create table comment (
       comment_id bigint not null auto_increment,
        is_deleted bit not null,
        text varchar(255),
        timestamp date not null,
        belongs_to_post_post_id bigint,
        belongs_to_user_user_id bigint,
        parent_comment_comment_id bigint,
        primary key (comment_id)
    ) engine=InnoDB;

    create table community (
       community_id bigint not null auto_increment,
        creation_date date not null,
        description varchar(255),
        is_suspended bit not null,
        name varchar(255) not null,
        suspension_reason varchar(255),
        primary key (community_id)
    ) engine=InnoDB;

    create table community_flairs (
       community_community_id bigint not null,
        flairs_flair_id bigint not null,
        primary key (community_community_id, flairs_flair_id)
    ) engine=InnoDB;

    create table flair (
       flair_id bigint not null auto_increment,
        name varchar(255),
        primary key (flair_id)
    ) engine=InnoDB;

    create table flair_communities (
       flair_flair_id bigint not null,
        communities_community_id bigint not null,
        primary key (flair_flair_id, communities_community_id)
    ) engine=InnoDB;

    create table moderator (
       u_id bigint not null,
        commu_id bigint not null,
        primary key (u_id, commu_id)
    ) engine=InnoDB;

    create table post (
       post_id bigint not null auto_increment,
        creation_date date not null,
        image_path varchar(255),
        text varchar(255),
        title varchar(255) not null,
        community_id bigint,
        flair_id bigint,
        user_id bigint,
        primary key (post_id)
    ) engine=InnoDB;

    create table reaction (
       reaction_id bigint not null auto_increment,
        timestamp date,
        type varchar(255) not null,
        made_by_user_id bigint,
        to_comment_comment_id bigint,
        to_post_post_id bigint,
        primary key (reaction_id)
    ) engine=InnoDB;

    create table report (
       report_id bigint not null auto_increment,
        accepted bit,
        reason varchar(255) not null,
        timestamp date,
        by_user_user_id bigint,
        for_comment_comment_id bigint,
        for_post_post_id bigint,
        primary key (report_id)
    ) engine=InnoDB;

    create table rule (
       rule_id bigint not null auto_increment,
        belongs_to_community_community_id bigint,
        primary key (rule_id)
    ) engine=InnoDB;

    create table user (
       role varchar(31) not null,
        user_id bigint not null auto_increment,
        avatar varchar(255),
        description varchar(255),
        display_name varchar(255),
        email varchar(255),
        password varchar(255) not null,
        registration_date date not null,
        username varchar(255) not null,
        primary key (user_id)
    ) engine=InnoDB;

    alter table user 
       add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);

    alter table banned 
       add constraint FKa836rewdg8n0a2vrayrkqb60l 
       foreign key (banned_by_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK5wswre9sf1uu41h2ifkhrfjqw 
       foreign key (banned_user_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK574c1sm21w3h7m90gu8k8jd5n 
       foreign key (for_community_community_id) 
       references community (community_id);

    alter table comment 
       add constraint FK4pwsbq31a29r5fds2nqvtopwg 
       foreign key (belongs_to_post_post_id) 
       references post (post_id);

    alter table comment 
       add constraint FK8mojd6ch4drippcsb6pk2mp9v 
       foreign key (belongs_to_user_user_id) 
       references user (user_id);

    alter table comment 
       add constraint FKc43l7bomr9sf0si605of2aakc 
       foreign key (parent_comment_comment_id) 
       references comment (comment_id);

    alter table community_flairs 
       add constraint FKpasiuv71rm8p4fr88busfcnqk 
       foreign key (flairs_flair_id) 
       references flair (flair_id);

    alter table community_flairs 
       add constraint FK9llkab37jr25pcw4v8fulslgj 
       foreign key (community_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKkdtoj42e43tf36qf359iebf8u 
       foreign key (communities_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKhwf3p8i50edrttqtmca1goab3 
       foreign key (flair_flair_id) 
       references flair (flair_id);

    alter table moderator 
       add constraint FK3jw301te4vawnv0nnjqbkoclx 
       foreign key (commu_id) 
       references community (community_id);

    alter table moderator 
       add constraint FK6viwndd99apby757x1bk2jfrt 
       foreign key (u_id) 
       references user (user_id);

    alter table post 
       add constraint FKokm06ignilxux2n1anwepgun7 
       foreign key (community_id) 
       references community (community_id);

    alter table post 
       add constraint FK6m84rx2dwjhng5qvfhq0rqadd 
       foreign key (flair_id) 
       references flair (flair_id);

    alter table post 
       add constraint FK72mt33dhhs48hf9gcqrq4fxte 
       foreign key (user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FKhs2tfxmmbtn5e5yfn537p2h5j 
       foreign key (made_by_user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FK8br49vcjbugdpwedd0bknwwtw 
       foreign key (to_comment_comment_id) 
       references comment (comment_id);

    alter table reaction 
       add constraint FKso87s37i14ldnmj9latpmd491 
       foreign key (to_post_post_id) 
       references post (post_id);

    alter table report 
       add constraint FKb6bt0hd02jy69jc52pn14creg 
       foreign key (by_user_user_id) 
       references user (user_id);

    alter table report 
       add constraint FKi0cmoo16sg6efbnnyfwcgm8qg 
       foreign key (for_comment_comment_id) 
       references comment (comment_id);

    alter table report 
       add constraint FKri5qf0b11l06wc2pcq0fj1l82 
       foreign key (for_post_post_id) 
       references post (post_id);

    alter table rule 
       add constraint FK8yk9btibk8xau3awcf39dms18 
       foreign key (belongs_to_community_community_id) 
       references community (community_id);

    create table banned (
       ban_id bigint not null auto_increment,
        timestamp date,
        banned_by_user_id bigint,
        banned_user_user_id bigint,
        for_community_community_id bigint,
        primary key (ban_id)
    ) engine=InnoDB;

    create table comment (
       comment_id bigint not null auto_increment,
        is_deleted bit not null,
        text varchar(255),
        timestamp date not null,
        belongs_to_post_post_id bigint,
        belongs_to_user_user_id bigint,
        parent_comment_comment_id bigint,
        primary key (comment_id)
    ) engine=InnoDB;

    create table community (
       community_id bigint not null auto_increment,
        creation_date date not null,
        description varchar(255),
        is_suspended bit not null,
        name varchar(255) not null,
        suspension_reason varchar(255),
        primary key (community_id)
    ) engine=InnoDB;

    create table community_flairs (
       community_community_id bigint not null,
        flairs_flair_id bigint not null,
        primary key (community_community_id, flairs_flair_id)
    ) engine=InnoDB;

    create table flair (
       flair_id bigint not null auto_increment,
        name varchar(255),
        primary key (flair_id)
    ) engine=InnoDB;

    create table flair_communities (
       flair_flair_id bigint not null,
        communities_community_id bigint not null,
        primary key (flair_flair_id, communities_community_id)
    ) engine=InnoDB;

    create table moderator (
       u_id bigint not null,
        commu_id bigint not null,
        primary key (u_id, commu_id)
    ) engine=InnoDB;

    create table post (
       post_id bigint not null auto_increment,
        creation_date date not null,
        image_path varchar(255),
        text varchar(255),
        title varchar(255) not null,
        community_id bigint,
        flair_id bigint,
        user_id bigint,
        primary key (post_id)
    ) engine=InnoDB;

    create table reaction (
       reaction_id bigint not null auto_increment,
        timestamp date,
        type varchar(255) not null,
        made_by_user_id bigint,
        to_comment_comment_id bigint,
        to_post_post_id bigint,
        primary key (reaction_id)
    ) engine=InnoDB;

    create table report (
       report_id bigint not null auto_increment,
        accepted bit,
        reason varchar(255) not null,
        timestamp date,
        by_user_user_id bigint,
        for_comment_comment_id bigint,
        for_post_post_id bigint,
        primary key (report_id)
    ) engine=InnoDB;

    create table rule (
       rule_id bigint not null auto_increment,
        belongs_to_community_community_id bigint,
        primary key (rule_id)
    ) engine=InnoDB;

    create table user (
       role varchar(31) not null,
        user_id bigint not null auto_increment,
        avatar varchar(255),
        description varchar(255),
        display_name varchar(255),
        email varchar(255),
        password varchar(255) not null,
        registration_date date not null,
        username varchar(255) not null,
        primary key (user_id)
    ) engine=InnoDB;

    alter table user 
       add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);

    alter table banned 
       add constraint FKa836rewdg8n0a2vrayrkqb60l 
       foreign key (banned_by_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK5wswre9sf1uu41h2ifkhrfjqw 
       foreign key (banned_user_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK574c1sm21w3h7m90gu8k8jd5n 
       foreign key (for_community_community_id) 
       references community (community_id);

    alter table comment 
       add constraint FK4pwsbq31a29r5fds2nqvtopwg 
       foreign key (belongs_to_post_post_id) 
       references post (post_id);

    alter table comment 
       add constraint FK8mojd6ch4drippcsb6pk2mp9v 
       foreign key (belongs_to_user_user_id) 
       references user (user_id);

    alter table comment 
       add constraint FKc43l7bomr9sf0si605of2aakc 
       foreign key (parent_comment_comment_id) 
       references comment (comment_id);

    alter table community_flairs 
       add constraint FKpasiuv71rm8p4fr88busfcnqk 
       foreign key (flairs_flair_id) 
       references flair (flair_id);

    alter table community_flairs 
       add constraint FK9llkab37jr25pcw4v8fulslgj 
       foreign key (community_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKkdtoj42e43tf36qf359iebf8u 
       foreign key (communities_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKhwf3p8i50edrttqtmca1goab3 
       foreign key (flair_flair_id) 
       references flair (flair_id);

    alter table moderator 
       add constraint FK3jw301te4vawnv0nnjqbkoclx 
       foreign key (commu_id) 
       references community (community_id);

    alter table moderator 
       add constraint FK6viwndd99apby757x1bk2jfrt 
       foreign key (u_id) 
       references user (user_id);

    alter table post 
       add constraint FKokm06ignilxux2n1anwepgun7 
       foreign key (community_id) 
       references community (community_id);

    alter table post 
       add constraint FK6m84rx2dwjhng5qvfhq0rqadd 
       foreign key (flair_id) 
       references flair (flair_id);

    alter table post 
       add constraint FK72mt33dhhs48hf9gcqrq4fxte 
       foreign key (user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FKhs2tfxmmbtn5e5yfn537p2h5j 
       foreign key (made_by_user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FK8br49vcjbugdpwedd0bknwwtw 
       foreign key (to_comment_comment_id) 
       references comment (comment_id);

    alter table reaction 
       add constraint FKso87s37i14ldnmj9latpmd491 
       foreign key (to_post_post_id) 
       references post (post_id);

    alter table report 
       add constraint FKb6bt0hd02jy69jc52pn14creg 
       foreign key (by_user_user_id) 
       references user (user_id);

    alter table report 
       add constraint FKi0cmoo16sg6efbnnyfwcgm8qg 
       foreign key (for_comment_comment_id) 
       references comment (comment_id);

    alter table report 
       add constraint FKri5qf0b11l06wc2pcq0fj1l82 
       foreign key (for_post_post_id) 
       references post (post_id);

    alter table rule 
       add constraint FK8yk9btibk8xau3awcf39dms18 
       foreign key (belongs_to_community_community_id) 
       references community (community_id);

    create table banned (
       ban_id bigint not null auto_increment,
        timestamp date,
        banned_by_user_id bigint,
        banned_user_user_id bigint,
        for_community_community_id bigint,
        primary key (ban_id)
    ) engine=InnoDB;

    create table comment (
       comment_id bigint not null auto_increment,
        is_deleted bit not null,
        text varchar(255),
        timestamp date not null,
        belongs_to_post_post_id bigint,
        belongs_to_user_user_id bigint,
        parent_comment_comment_id bigint,
        primary key (comment_id)
    ) engine=InnoDB;

    create table community (
       community_id bigint not null auto_increment,
        creation_date date not null,
        description varchar(255),
        is_suspended bit not null,
        name varchar(255) not null,
        suspension_reason varchar(255),
        primary key (community_id)
    ) engine=InnoDB;

    create table community_flairs (
       community_community_id bigint not null,
        flairs_flair_id bigint not null,
        primary key (community_community_id, flairs_flair_id)
    ) engine=InnoDB;

    create table flair (
       flair_id bigint not null auto_increment,
        name varchar(255),
        primary key (flair_id)
    ) engine=InnoDB;

    create table flair_communities (
       flair_flair_id bigint not null,
        communities_community_id bigint not null,
        primary key (flair_flair_id, communities_community_id)
    ) engine=InnoDB;

    create table moderator (
       u_id bigint not null,
        commu_id bigint not null,
        primary key (u_id, commu_id)
    ) engine=InnoDB;

    create table post (
       post_id bigint not null auto_increment,
        creation_date date not null,
        image_path varchar(255),
        text varchar(255),
        title varchar(255) not null,
        community_id bigint,
        flair_id bigint,
        user_id bigint,
        primary key (post_id)
    ) engine=InnoDB;

    create table reaction (
       reaction_id bigint not null auto_increment,
        timestamp date,
        type varchar(255) not null,
        made_by_user_id bigint,
        to_comment_comment_id bigint,
        to_post_post_id bigint,
        primary key (reaction_id)
    ) engine=InnoDB;

    create table report (
       report_id bigint not null auto_increment,
        accepted bit,
        reason varchar(255) not null,
        timestamp date,
        by_user_user_id bigint,
        for_comment_comment_id bigint,
        for_post_post_id bigint,
        primary key (report_id)
    ) engine=InnoDB;

    create table rule (
       rule_id bigint not null auto_increment,
        belongs_to_community_community_id bigint,
        primary key (rule_id)
    ) engine=InnoDB;

    create table user (
       role varchar(31) not null,
        user_id bigint not null auto_increment,
        avatar varchar(255),
        description varchar(255),
        display_name varchar(255),
        email varchar(255),
        password varchar(255) not null,
        registration_date date not null,
        username varchar(255) not null,
        primary key (user_id)
    ) engine=InnoDB;

    alter table user 
       add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);

    alter table banned 
       add constraint FKa836rewdg8n0a2vrayrkqb60l 
       foreign key (banned_by_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK5wswre9sf1uu41h2ifkhrfjqw 
       foreign key (banned_user_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK574c1sm21w3h7m90gu8k8jd5n 
       foreign key (for_community_community_id) 
       references community (community_id);

    alter table comment 
       add constraint FK4pwsbq31a29r5fds2nqvtopwg 
       foreign key (belongs_to_post_post_id) 
       references post (post_id);

    alter table comment 
       add constraint FK8mojd6ch4drippcsb6pk2mp9v 
       foreign key (belongs_to_user_user_id) 
       references user (user_id);

    alter table comment 
       add constraint FKc43l7bomr9sf0si605of2aakc 
       foreign key (parent_comment_comment_id) 
       references comment (comment_id);

    alter table community_flairs 
       add constraint FKpasiuv71rm8p4fr88busfcnqk 
       foreign key (flairs_flair_id) 
       references flair (flair_id);

    alter table community_flairs 
       add constraint FK9llkab37jr25pcw4v8fulslgj 
       foreign key (community_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKkdtoj42e43tf36qf359iebf8u 
       foreign key (communities_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKhwf3p8i50edrttqtmca1goab3 
       foreign key (flair_flair_id) 
       references flair (flair_id);

    alter table moderator 
       add constraint FK3jw301te4vawnv0nnjqbkoclx 
       foreign key (commu_id) 
       references community (community_id);

    alter table moderator 
       add constraint FK6viwndd99apby757x1bk2jfrt 
       foreign key (u_id) 
       references user (user_id);

    alter table post 
       add constraint FKokm06ignilxux2n1anwepgun7 
       foreign key (community_id) 
       references community (community_id);

    alter table post 
       add constraint FK6m84rx2dwjhng5qvfhq0rqadd 
       foreign key (flair_id) 
       references flair (flair_id);

    alter table post 
       add constraint FK72mt33dhhs48hf9gcqrq4fxte 
       foreign key (user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FKhs2tfxmmbtn5e5yfn537p2h5j 
       foreign key (made_by_user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FK8br49vcjbugdpwedd0bknwwtw 
       foreign key (to_comment_comment_id) 
       references comment (comment_id);

    alter table reaction 
       add constraint FKso87s37i14ldnmj9latpmd491 
       foreign key (to_post_post_id) 
       references post (post_id);

    alter table report 
       add constraint FKb6bt0hd02jy69jc52pn14creg 
       foreign key (by_user_user_id) 
       references user (user_id);

    alter table report 
       add constraint FKi0cmoo16sg6efbnnyfwcgm8qg 
       foreign key (for_comment_comment_id) 
       references comment (comment_id);

    alter table report 
       add constraint FKri5qf0b11l06wc2pcq0fj1l82 
       foreign key (for_post_post_id) 
       references post (post_id);

    alter table rule 
       add constraint FK8yk9btibk8xau3awcf39dms18 
       foreign key (belongs_to_community_community_id) 
       references community (community_id);

    create table banned (
       ban_id bigint not null auto_increment,
        timestamp date,
        banned_by_user_id bigint,
        banned_user_user_id bigint,
        for_community_community_id bigint,
        primary key (ban_id)
    ) engine=InnoDB;

    create table comment (
       comment_id bigint not null auto_increment,
        is_deleted bit not null,
        text varchar(255),
        timestamp date not null,
        belongs_to_post_post_id bigint,
        belongs_to_user_user_id bigint,
        parent_comment_comment_id bigint,
        primary key (comment_id)
    ) engine=InnoDB;

    create table community (
       community_id bigint not null auto_increment,
        creation_date date not null,
        description varchar(255),
        is_suspended bit not null,
        name varchar(255) not null,
        suspension_reason varchar(255),
        primary key (community_id)
    ) engine=InnoDB;

    create table community_flairs (
       community_community_id bigint not null,
        flairs_flair_id bigint not null,
        primary key (community_community_id, flairs_flair_id)
    ) engine=InnoDB;

    create table flair (
       flair_id bigint not null auto_increment,
        name varchar(255),
        primary key (flair_id)
    ) engine=InnoDB;

    create table flair_communities (
       flair_flair_id bigint not null,
        communities_community_id bigint not null,
        primary key (flair_flair_id, communities_community_id)
    ) engine=InnoDB;

    create table moderator (
       u_id bigint not null,
        commu_id bigint not null,
        primary key (u_id, commu_id)
    ) engine=InnoDB;

    create table post (
       post_id bigint not null auto_increment,
        creation_date date not null,
        image_path varchar(255),
        text varchar(255),
        title varchar(255) not null,
        community_id bigint,
        flair_id bigint,
        user_id bigint,
        primary key (post_id)
    ) engine=InnoDB;

    create table reaction (
       reaction_id bigint not null auto_increment,
        timestamp date,
        type varchar(255) not null,
        made_by_user_id bigint,
        to_comment_comment_id bigint,
        to_post_post_id bigint,
        primary key (reaction_id)
    ) engine=InnoDB;

    create table report (
       report_id bigint not null auto_increment,
        accepted bit,
        reason varchar(255) not null,
        timestamp date,
        by_user_user_id bigint,
        for_comment_comment_id bigint,
        for_post_post_id bigint,
        primary key (report_id)
    ) engine=InnoDB;

    create table rule (
       rule_id bigint not null auto_increment,
        belongs_to_community_community_id bigint,
        primary key (rule_id)
    ) engine=InnoDB;

    create table user (
       role varchar(31) not null,
        user_id bigint not null auto_increment,
        avatar varchar(255),
        description varchar(255),
        display_name varchar(255),
        email varchar(255),
        password varchar(255) not null,
        registration_date date not null,
        username varchar(255) not null,
        primary key (user_id)
    ) engine=InnoDB;

    alter table user 
       add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);

    alter table banned 
       add constraint FKa836rewdg8n0a2vrayrkqb60l 
       foreign key (banned_by_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK5wswre9sf1uu41h2ifkhrfjqw 
       foreign key (banned_user_user_id) 
       references user (user_id);

    alter table banned 
       add constraint FK574c1sm21w3h7m90gu8k8jd5n 
       foreign key (for_community_community_id) 
       references community (community_id);

    alter table comment 
       add constraint FK4pwsbq31a29r5fds2nqvtopwg 
       foreign key (belongs_to_post_post_id) 
       references post (post_id);

    alter table comment 
       add constraint FK8mojd6ch4drippcsb6pk2mp9v 
       foreign key (belongs_to_user_user_id) 
       references user (user_id);

    alter table comment 
       add constraint FKc43l7bomr9sf0si605of2aakc 
       foreign key (parent_comment_comment_id) 
       references comment (comment_id);

    alter table community_flairs 
       add constraint FKpasiuv71rm8p4fr88busfcnqk 
       foreign key (flairs_flair_id) 
       references flair (flair_id);

    alter table community_flairs 
       add constraint FK9llkab37jr25pcw4v8fulslgj 
       foreign key (community_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKkdtoj42e43tf36qf359iebf8u 
       foreign key (communities_community_id) 
       references community (community_id);

    alter table flair_communities 
       add constraint FKhwf3p8i50edrttqtmca1goab3 
       foreign key (flair_flair_id) 
       references flair (flair_id);

    alter table moderator 
       add constraint FK3jw301te4vawnv0nnjqbkoclx 
       foreign key (commu_id) 
       references community (community_id);

    alter table moderator 
       add constraint FK6viwndd99apby757x1bk2jfrt 
       foreign key (u_id) 
       references user (user_id);

    alter table post 
       add constraint FKokm06ignilxux2n1anwepgun7 
       foreign key (community_id) 
       references community (community_id);

    alter table post 
       add constraint FK6m84rx2dwjhng5qvfhq0rqadd 
       foreign key (flair_id) 
       references flair (flair_id);

    alter table post 
       add constraint FK72mt33dhhs48hf9gcqrq4fxte 
       foreign key (user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FKhs2tfxmmbtn5e5yfn537p2h5j 
       foreign key (made_by_user_id) 
       references user (user_id);

    alter table reaction 
       add constraint FK8br49vcjbugdpwedd0bknwwtw 
       foreign key (to_comment_comment_id) 
       references comment (comment_id);

    alter table reaction 
       add constraint FKso87s37i14ldnmj9latpmd491 
       foreign key (to_post_post_id) 
       references post (post_id);

    alter table report 
       add constraint FKb6bt0hd02jy69jc52pn14creg 
       foreign key (by_user_user_id) 
       references user (user_id);

    alter table report 
       add constraint FKi0cmoo16sg6efbnnyfwcgm8qg 
       foreign key (for_comment_comment_id) 
       references comment (comment_id);

    alter table report 
       add constraint FKri5qf0b11l06wc2pcq0fj1l82 
       foreign key (for_post_post_id) 
       references post (post_id);

    alter table rule 
       add constraint FK8yk9btibk8xau3awcf39dms18 
       foreign key (belongs_to_community_community_id) 
       references community (community_id);
