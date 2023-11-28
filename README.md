[![rugby-info-api-ci](https://github.com/Satoru-Oki/RugbyInfoApplication/actions/workflows/ci.yml/badge.svg)](https://github.com/Satoru-Oki/RugbyInfoApplication/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/Satoru-Oki/RugbyInfoApplication/graph/badge.svg?token=HWS58KBUOY)](https://codecov.io/gh/Satoru-Oki/RugbyInfoApplication)


## RugbyInfoApplication
<img width="600" alt="image" src="https://github.com/Satoru-Oki/RugbyInfoApplication/assets/143796169/cee51615-75a5-4347-995b-c433f1bdd26d">

### URL
https://rugby-player-info.com/rugbyPlayers

### 概要  
外部APIからラグビーワールドカップ出場国選手のデータを取得、取得データおよび任意のチーム・選手を表示・検索・登録・修正できるアプリケーション

- 機能  
  - Rugby_v3　"https://developer.sportradar.com/docs/read/rugby/Rugby_v3"  からデータ取得  
  - ラグビーワールドカップ2023出場20ヶ国の選手データを利用できる
  - チーム毎に選手を表示できる（名前・身長・体重・ポジションを表示）
  - 身長・体重・ポジションの組み合わせで選手を検索できる
  - 国別にFW（フォワード）BK（バックス）の平均身長、体重を表示できる
  - 新しいチーム,選手を登録できる
  - 既存の選手の修正,削除ができる

### 作成経緯
　2023年ラグビーワールドカップ、日本代表のみならず各国チームは心が動くゲームを見せてくれました。４年に一度の機会に選手たちは持てる力のすべてをかけます。  
　私はこれまでの環境を離れ、あらたな道へ進もうとしています。その第一歩としてこの大会にちなんだアプリケーションを作成することとしました。

`ラグビーは少年をいち早く大人にし、大人に永遠に少年の魂を抱かせる`  
　by Jean-Pierre Rives （1975-1984　フランス代表キャプテン）

### Application 概要図
![概要図ファイル drawio (2)](https://github.com/Satoru-Oki/RugbyInfoApplication/assets/143796169/75bfec0e-e97d-466b-a8bd-8e030ddafc21)

### 主な技術構成
- Java 17
- Spring Boot 3.1.4
- Thymeleaf 3.1.2
- Tomcat(Embedded) 10.1.13
- MyBatis 3.5.13
- MySQL 8.0
- Docker 24.0.6
- その他
  - CI（自動テスト,Checkstyle,Codecov）
    
### DB定義
テーブル名：rugby_players_world_cup
|カラム名|データ型|キー|備考|
| ---- | ---- | ---- | ---- |
| id | VARCHAR(30) | PRIMARY KEY | 登録時自動生成
| nationality | VARCHAR(30)  
| name | VARCHAR(50) 
| height | INT(5) 
| weight | INT(5) 
| rugby_position | VARCHAR(50) 

### URL一覧
| 機能 | 詳細 | URL | 備考 |
| ---- | ---- |----|----|
| チーム別検索 | 国別に選手を検索 |/rugbyPlayers?nationality={nationality} |parumはnullを許容
| 選手検索 | 身長・体重・ポジションの組み合わせで選手を検索|/rugbyPlayers/reference?height={height}&weight={weight}&rugbyPosition={rugbyPosition}|parumはnullを許容
| チーム別平均データ | チーム毎にFW、BKの平均身長・体重を表示 |/rugbyPlayers/average
| 選手登録 | 選手を登録 |/rugbyPlayers/new | モーダル
| 選手修正 | 選手データを更新 |/rugbyPlayers/edit/{id} | モーダル
| 選手削除 | 選手データを削除 |/rugbyPlayers/delete/{id} 

### インフラ構成図
![インフラ構成図 drawio (3)](https://github.com/Satoru-Oki/RugbyInfoApplication/assets/143796169/1129c54b-2176-4033-8fa0-d1cffaa41063)

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
- 選手データテーブルの追加（試合でのパフォーマンスデータ）
- パフォーマンスデータの平均との比較、top5の表示
など



