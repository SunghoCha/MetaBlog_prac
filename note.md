# Hodolog prac

##  댓글 -> 테이블 모델링 (comment) (= Comment Entity)

## 비공개, 공개 여부 (상태값) -> (Enum)

## 카테고리 -> DB(or Enum)

## 로그인 -> spring security

## 비밀번호 암호화

1. 해시
2. 해시 방식
    - SHA1
    - SHA256
    - MD5
    - ==> 이것들로 비번 암호화하면 안됨. 이유 알아두기
      (*참고 - salt값이 없어서 input이 같으면 항상 ootput도 같음)
3. Bcrypt, Scrypt, Argon2(~salt값)


