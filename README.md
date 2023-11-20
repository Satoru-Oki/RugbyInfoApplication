[![codecov](https://codecov.io/gh/Satoru-Oki/RugbyInfoApplication/graph/badge.svg?token=HWS58KBUOY)](https://codecov.io/gh/Satoru-Oki/RugbyInfoApplication)

## RugbyInfoApplication
<img width="600" alt="image" src="https://github.com/Satoru-Oki/RugbyInfoApplication/assets/143796169/cee51615-75a5-4347-995b-c433f1bdd26d">

### 概要  
外部APIからラグビーワールドカップ出場国選手のデータを取得、取得データおよび任意のチーム・選手を表示・検索・登録・修正できるアプリケーション

- 機能  
  - Rugby_v3　"https://developer.sportradar.com/docs/read/rugby/Rugby_v3"  からデータ取得  
  - ラグビーワールドカップ2023出場20ヶ国の選手データを利用できる
  - チーム毎に選手を表示できる（名前・身長・体重・ポジションを表示）
  - 身長・体重・ポジションの組み合わせで選手を検索できる
  - 国別にFW（フォワード）,BK（バックス）の平均身長、体重を表示できる
  - 新しいチーム,選手を登録できる
  - 既存の選手の修正,削除ができる

### Application 概要図
![概要図ファイル drawio (2)](https://github.com/Satoru-Oki/RugbyInfoApplication/assets/143796169/75bfec0e-e97d-466b-a8bd-8e030ddafc21)

### 技術構成
- Java 17
- Spring Boot 3.1.4
- Thymeleaf 3.1.2
- Tomcat(Embedded) 10.1.13
- MyBatis 3.5.13
- MySQL 8.0
- Docker 24.0.6
  
### DB定義
テーブル名：rugby_players_world_cup
|カラム名|データ型|キー|備考|
| ---- | ---- | ---- | ----|
| id | VARCHAR(30) | PRIMARY KEY|登録時自動生成|
| nationality | VARCHAR(30) | |
| name | VARCHAR(50) |||
| height | INT(5) |
| weight | INT(5) |
| rugby_position | VARCHAR(50) ||

### URL一覧

<img width="567" alt="image" src="https://github.com/Satoru-Oki/RugbyInfoApplication/assets/143796169/d6674197-c633-4fc1-90bf-929372c8c38e">

### DEMO
チーム別選手表示 

![表示 (6)](https://github.com/Satoru-Oki/RugbyInfoApplication/assets/143796169/a04ea8da-1d5f-4017-b02e-8e3b0336fab7) 

選手検索 

![選手検索 (1)](https://github.com/Satoru-Oki/RugbyInfoApplication/assets/143796169/a1b53bb0-a234-488d-a6e9-b0352dd03871)

チーム別平均データ

![チーム別平均データ](https://github.com/Satoru-Oki/RugbyInfoApplication/assets/143796169/eb456ad0-577b-41b9-9726-0ab58d47b1d6)

選手更新/削除

![選手更新削除](https://github.com/Satoru-Oki/RugbyInfoApplication/assets/143796169/2d4a0768-5860-4c54-8f6c-6c636a0b51e2)

選手登録

![選手登録 (1)](https://github.com/Satoru-Oki/RugbyInfoApplication/assets/143796169/3b1fa986-e893-498d-b1e7-0b2a00e3fc2b)

### 今後について
- デプロイ
- Https化
など



