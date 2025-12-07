-- 1. 관리자 회원 데이터 (ID가 1인 회원이 없으면 추가)
INSERT IGNORE INTO member (id, username, password, email, created_at) 
VALUES (1, 'manager', '1234', 'manager@dorm.com', NOW());

-- 2. 게시글 데이터 (ID가 중복되면 무시)
INSERT IGNORE INTO post (id, category, title, content, member_id, created_at) VALUES 
(1, 'group', '휴지 공구하실 분 (3/4)', '같이 사요 싸게', 1, NOW()),
(2, 'group', '배달비 나눔하실 분', '치킨 시킬건데..', 1, NOW()),
(3, 'review', '기숙사 식당 솔직 후기', '맛있어요', 1, NOW()),
(4, 'review', '새로 산 스탠드 좋아요', '눈이 안 아픔', 1, NOW()),
(5, 'recipe', '편의점 꿀조합', '불닭+치즈', 1, NOW()),
(6, 'tip', '빨래 건조기 시간 꿀팁', '저녁엔 사람 많아요', 1, NOW()),
(7, 'counseling', '룸메가 밤마다 노래 불러요', '어떡하죠', 1, NOW());