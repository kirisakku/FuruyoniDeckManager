# FuruyoniDeckManager
ふるよにのデッキを管理するアプリのリポジトリ（※開発中）

本アプリは以下の画像を用いております。
（※再配布を防ぐために、本リポジトリには登録しておりません）

* ふるよにコモンズ/BakaFire,TOKIAME （https://main-bakafire.ssl-lolipop.jp/furuyoni/na/rule.html）

## リリースノート
### 1.5.1(2024/01/18)
* A1トコヨの奏流しのカード画像が古い不良の修正
* カード一覧にミソラのカードが表示されない不良の修正

### v1.5(2024/01/09)
* シーズン9に対応
* デッキ編集機能を追加
* アクションバーにホーム画面に戻るためのボタンを追加

### v1.4(2023/07/22）
* カード種別ごとにカード一覧を眺められる機能を実装
* シーズン8-2に対応

### v1.3(2023/03/19）
* カード一覧参照機能を追加（トップ画面から行けます）

### v1.2(2023/03/18)
* 小さいスマホでもレイアウトが崩れないように対応
* シスイの一部カード名の誤植を修正
* オリジンとアナザー間で切り替えを行った際に枚数が正しく数えられない不良の修正

## 使い方
https://github.com/kirisakku/FuruyoniDeckManager/releases からapp-release.apkをダウンロード後、インストールしてください。

**注意**
* 個人利用用に作成したため、Google Playストアでは公開しておりません。自己責任でインストールし、使用してください
* バージョンアップが上手く行かない現象を確認しております。こちら、原因調査中です。別アプリとしてインストールすることは可能のため、アプリIDを変更したビルド結果をご希望の方は https://twitter.com/kirisakku までご連絡ください。

インストール後、アプリを起動してください。本アプリは「デッキ登録」と「デッキ参照」、「カード一覧」の3つのモードに大きく分かれています。

<img src="https://user-images.githubusercontent.com/41281605/226122290-c409ff10-8beb-4051-a227-6a9972b20fac.jpg" width="250px">

何もデッキを登録していない状態で「デッキ参照」を選択すると、以下の通りデッキ登録を促されます。まずは「デッキ登録」からデッキを登録してください。

<img src="https://user-images.githubusercontent.com/41281605/205489535-c650404a-3883-401e-9c22-e701e65e14ec.png" width="250px">

「デッキ登録」画面ではまず、メガミを2人選択する必要があります。
2人選択後、画面下にある「デッキ構築開始」を選択してください。
選択されている人数が2人以外の場合は、「デッキ構築開始」ボタンは非活性のため、押すことができません。
選択したメガミはピンク枠で囲まれます。もう1度選択すると、選択解除されます。

<img src="https://user-images.githubusercontent.com/41281605/205489727-03822e0a-3dc8-48a7-b09a-4d25b1455952.png" width="250px">

「デッキ構築開始」ボタンを選択すると、カード選択画面に遷移します。
ここでは通常札7枚、切札3枚を選択する必要があります。チェックボックスにチェックを入れ、カードを選択してください。
カード名のボタンをタップすることにより、実際のカードを確認することができます。

画面下部に「＋」ボタンがある場合は、それをタップすることにより、追加札の一覧を見ることができます。
また、画面上部にキャラクターのアイコンが複数表示されている場合は、別なアイコンをタップすることによって、アナザー版メガミに切り替えることができます。
アナザー版にすることによって切り替わったカードは、ボタンの色がピンク色になります。

通常札を7枚、切札を3枚選択すると画面下部の「デッキを登録」ボタンが活性状態になります。

<img src="https://user-images.githubusercontent.com/41281605/205489907-05a8b158-538c-422f-beff-dc642a1f0196.png" width="250px">

「デッキを登録」ボタンをタップすると、デッキ名の入力ダイアログが表示されます。
1文字～15文字の範囲で自由にデッキ名を入力してください。

**注意**
2022/12/04時点で、英字の小文字が大文字に自動変換されてしまうバグがあります。

<img src="https://user-images.githubusercontent.com/41281605/205490010-1e2ae616-9766-47e4-b58e-a79bc17880d6.png" width="250px">

デッキを登録すると、自動的に「デッキ参照」モードに遷移します。

<img src="https://user-images.githubusercontent.com/41281605/205490151-31c6283d-0b97-4e46-b107-bbf49391f05a.png" width="250px">

「デッキ参照」モードでデッキ名ボタンをタップすると、作成したデッキが表示されます。

<img src="https://user-images.githubusercontent.com/41281605/205490191-c8e8e086-c069-4210-a510-5b59b8833fbe.png" width="250px">

画面右上の鉛筆アイコンをタップすることで、デッキの編集画面に遷移することができます。

<img src="https://github.com/kirisakku/FuruyoniDeckManager/assets/41281605/63dfeaf0-8cee-4374-80b1-5cf09e4954b8" width="250px">

画面下部の緑色の領域をタップすることで、デッキに関するコメントを残すことができます。
是非、実際にデッキを使ってみて見つかった改善点等を記載してください。

<img src="https://user-images.githubusercontent.com/41281605/205490251-0b257dae-39ab-476b-b568-9b8c3676c651.png" width="250px">

作成したデッキを削除したい場合には「デッキ参照」モードで画面左側にある「×」ボタンをタップすることで、デッキを削除することができます。
確認ダイアログが表示されるので、問題がない場合は「削除」をタップしてください。

<img src="https://user-images.githubusercontent.com/41281605/205490292-d7f5336d-4b95-48c6-bc3d-d3c3a47b6331.png" width="250px">

「カード一覧」モードでは、メガミ種別またはカード種別ごとにカードの一覧を確認することができます。

メガミ種別ごとにカードの一覧を確認したい場合は「メガミ種別」をタップしてください。

タップ後に表示される画面から、カード一覧を確認したいメガミのアイコンをタップしてください。

<img src="https://user-images.githubusercontent.com/41281605/226122435-ad84f1b6-1bf2-4d5a-b228-a9ae041ce67f.jpg" width="250px">

タップしたメガミのカード一覧が表示されます。
カード選択画面同様、カード名をタップすることで、実際のカードを確認することができます。
画面下部に「＋」ボタンがある場合は、それをタップすることにより、追加札の一覧を見ることができます。
また、画面上部にキャラクターのアイコンが複数表示されている場合は、別なアイコンをタップすることによって、アナザー版メガミに切り替えることができます。
アナザー版にすることによって切り替わったカードは、ボタンの色がピンク色になります。

<img src="https://user-images.githubusercontent.com/41281605/226122558-59db43fa-0f6d-4abe-a640-5afa21551d1f.jpg" width="250px">

カード種別ごとにカードの一覧を確認したい場合は「カード種別」をタップしてください。

タップ後に表示される画面から、カード一覧を確認したい種別のボタンをタップしてください。

<img src="https://github.com/kirisakku/FuruyoniDeckManager/assets/41281605/424b9de1-d4c3-446e-b26c-768b9e0ed827" width="250px">

タップしたカード種別のカード一覧が表示されます。
カード選択画面同様、カード名をタップすることで、実際のカードを確認することができます。

<img src="https://github.com/kirisakku/FuruyoniDeckManager/assets/41281605/424b9de1-d4c3-446e-b26c-768b9e0ed827" width="250px">
