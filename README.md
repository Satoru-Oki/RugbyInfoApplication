## RugbyInfoApplication
<img width="600" alt="image" src="https://github.com/Satoru-Oki/RugbyInfoApplication/assets/143796169/cee51615-75a5-4347-995b-c433f1bdd26d">

### 概要  
外部のAPIに接続し、ラグビーワールドカップ出場国選手のデータを検索・閲覧でき、任意のチーム・選手を登録できるアプリケーション

- 機能  
  - Rugby_v3　"https://developer.sportradar.com/docs/read/rugby/Rugby_v3"  からデータ取得  
  - ラグビーワールドカップ2023出場20ヶ国の選手データを利用できる
  - チーム毎に選手を表示できる（名前・身長・体重・ポジションを表示）
  - 身長・体重・ポジションの組み合わせで選手を検索できる
  - 国別にFW（フォワード）,BK（バックス）の平均身長、体重を表示できる
  - 新しいチーム,選手を登録できる
  - 既存の選手の修正,削除ができる

### Application 概要図
![概要図ファイル drawio (1)](https://github.com/Satoru-Oki/RugbyInfoApplication/assets/143796169/1d09b93a-eef3-4317-b653-b74436bf56fa)

### 技術構成
- Java 17
- Spring Boot 3.1.4
- Thymeleaf 3.1.2
- Tomcat(Embedded) 10.1.13
- MyBatis 3.5.13
- MySQL 8.0
