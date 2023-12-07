use reddit;
select * from comment;
select * from reaction;

select count(r.reaction_id) from reaction r where r.to_comment_comment_id = 2 and r.type = 'UPVOTE';

--  Reaction-sorted Comments
select c.* 
from comment c 
where c.belongs_to_post_post_id = 2 
order by (select count(r.reaction_id) from reaction r where r.to_comment_comment_id = c.comment_id and r.type = 'UPVOTE') desc;

--  trending-sorted Posts
select p.*
from post p
where p.community_id = 1
order by (( (select count(r.reaction_id) 
from reaction r 
where r.to_post_post_id = p.post_id)*0.4) + (p.creation_date*0.6) ) desc;

-- delete reactions & reports for comments belonging to a to-be-deleted post 
delete from reaction 
where to_comment_comment_id in 
(select comment_id from comment where belongs_to_post_post_id = 1);

delete from report 
where for_comment_comment_id in 
(select comment_id from comment where belongs_to_post_post_id = 1);

-- update reaction set type = 'DOWNVOTE' where reaction_id = 8;

select sum(r.type='UPVOTE') - sum(r.type='DOWNVOTE')
from post p, reaction r 
where p.user_id = 2 and 
	r.to_post_post_id = p.post_id 
union 
select sum(r.type='UPVOTE') - sum(r.type='DOWNVOTE') 
from comment c, reaction r 
where c.belongs_to_user_user_id = 2 and 
	r.to_comment_comment_id = c.comment_id;
-------------------
select sum(u) - sum(d)
from
(select sum(r.type='UPVOTE') as u,  sum(r.type='DOWNVOTE') as d
from post p, reaction r 
where p.user_id = 1 and 
	r.to_post_post_id = p.post_id 
union 
select sum(r.type='UPVOTE') as u, sum(r.type='DOWNVOTE') as d
from comment c, reaction r 
where c.belongs_to_user_user_id = 1 and 
	r.to_comment_comment_id = c.comment_id);
---------------

-- select * 
-- from reaction 
-- where made_by_user_id is not null;

-- fetch all karma for user profile DOESN'T WORK AS INTENDED
-- select count(re.type) as "total_num" from reaction re, post p, comment c 
-- where 
-- (p.user_id = 1 and re.to_post_post_id = p.post_id) and 
-- (c.belongs_to_user_user_id = 1 and re.to_comment_comment_id = c.comment_id) 
-- group by re.type;


use reddit; 
select * from post;

insert into post (creation_date, text, title, community_id, user_id)
 values ('2022-10-11', 'So Mazda JP have just released this beauty of a teaser for a very special car of their past ... This is an official render of what MazdaSpeed should look like, ...', 'MazdaSpeed official teaser. The zoom-zoom is back!', 3, 1);

select
        p.* 
    from
        post p 
    where
        p.community_id = 3 
    order by
        p.creation_date desc;

select * from reaction;

delete from user where user_id = 4;
delete from comment where parent_comment_comment_id is not null;
delete from reaction where reaction_id = 19;

delete from moderator where u_id = 1 and commu_id = 9;

use redditdb;

select * from banned;
select * from community;
select * from moderator;
select * from user;
select * from comment;
select * from post;
select * from reaction;

update community set suspension_reason = null where community_id = 9;
update community set is_suspended = 0 where community_id = 9;

-- delete from user where user_id = 14;

select sum(r.type = 'UPVOTE') - sum(r.type = 'DOWNVOTE') 
from reaction r 
where r.to_comment_comment_id = 1;

select c.* from comment c where c.belongs_to_post_post_id = 1 order by timestamp desc;

-- update comment set timestamp = '2022-09-04' where comment_id = 2;
update comment set text = 'I dont care, I love it!' where comment_id = 1;

update user set role = "ADMIN" where username LIKE 'testdanilo';

insert into moderator (u_id, commu_id) values (1, 9);

insert into community (creation_date, description, is_suspended, name) values ('2022-10-11', 'Community exploring japanese culture, food & language.', false, 'Japanese Culture');
insert into community (creation_date, description, is_suspended, name) values ('2022-12-11', 'Made for JDM car & racing enthusiasts. We discuss custom JDM kits, compare different parts & usual JDM builds.', false, 'JDM cars');
insert into community (creation_date, description, is_suspended, suspension_reason, name) values ('2022-09-03', 'Our community is all about dogs. Dog owners along with everything from dog food, psychology & health to dog adoption & training programme announcements!', true, 'False dog adoption announcement baits. Community was frequently used for child trafficing & abuse.', 'Dogs');

insert into comment (is_deleted, text, timestamp, belongs_to_post_post_id, belongs_to_user_user_id) values (false, 'Wow, I just set my eyes on this one. It is going to be amazing! Cheers for sharing this mate!', '2022-09-04', 1, 1);
insert into comment (is_deleted, text, timestamp, belongs_to_post_post_id, belongs_to_user_user_id) values (false, 'Cool! Sooo... what are the power figures like? Has anyone heard anything yet?', '2022-09-05', 1, 4);

insert into post (creation_date, text, title, community_id, user_id) values ('2022-09-10', 'TestContent', 'TestTitleWhatever', 3, 1);

insert into user (role, description, display_name, email, password, registration_date, username) values ('USER', 'This is quite a short description of myself ...', 'ivyy', 'ivv19@gmail.com', '$2a$12$6CXwOK1MpeRHlm92LZJoy.ufNBl2JP9cyfGZA2HOkSjl/LEnRJwCK', '2022-10-11', 'ivv19');