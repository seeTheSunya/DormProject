-- H2 데이터베이스용 초기 데이터 (MySQL 문법 대신 H2 호환 문법 사용)

-- 1. 관리자 회원 데이터
INSERT INTO member (id, username, password, email, created_at, is_verified) 
VALUES (1, 'manager', '1234', 'manager@dorm.com', CURRENT_TIMESTAMP, false)
ON CONFLICT (id) DO NOTHING;

-- 2. 게시글 데이터
INSERT INTO post (id, category, title, content, member_id, created_at, like_count) VALUES 
(1, 'group', '휴지 공구하실 분 (3/4)', '같이 사요 싸게', 1, CURRENT_TIMESTAMP, 0),
(2, 'group', '배달비 나눔하실 분', '치킨 시킬건데..', 1, CURRENT_TIMESTAMP, 0),
(3, 'review', '기숙사 식당 솔직 후기', '맛있어요', 1, CURRENT_TIMESTAMP, 0),
(4, 'review', '새로 산 스탠드 좋아요', '눈이 안 아픔', 1, CURRENT_TIMESTAMP, 0),
(5, 'recipe', '편의점 꿀조합', '불닭+치즈', 1, CURRENT_TIMESTAMP, 0),
(6, 'tip', '빨래 건조기 시간 꿀팁', '저녁엔 사람 많아요', 1, CURRENT_TIMESTAMP, 0),
(7, 'counseling', '룸메가 밤마다 노래 불러요', '어떡하죠', 1, CURRENT_TIMESTAMP, 0)
ON CONFLICT (id) DO NOTHING;

