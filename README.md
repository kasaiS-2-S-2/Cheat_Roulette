# Name：イカサマできるルーレット

イカサマ機能のついたルーレットを作成できるアプリです。

このアプリを活用すると、レストラン決めや罰ゲーム決め等、様々な場面でバレずにイカサマを行うことができます。

※審査中なので、まだGoogle Playには公開できていません。

## Demo
![demo](https://raw.github.com/wiki/kasaiS-2-S-2/Cheat_Roulette/images/main2.gif)
![demo](https://raw.github.com/wiki/kasaiS-2-S-2/Cheat_Roulette/images/roulette_create.gif)

![demo](https://raw.github.com/wiki/kasaiS-2-S-2/Cheat_Roulette/images/myRoulette.gif)
![demo](https://raw.github.com/wiki/kasaiS-2-S-2/Cheat_Roulette/images/dark_mode.gif)

## 主な使用技術
・ConstraintLayout

・RecyclerView

・Room

・PreferenceFragment

## 特徴 & 工夫
・画面数：6画面

・ルーレット保存機能

・イカサマ設定機能

・シンプルなデザイン

・自然にイカサマを行えるようにした（ルーレットの回転が不自然に見えないようにした）

・できるだけイカサマが周りにバレないようにした（隠しボタンの実装等）

・ダークテーマ対応

## 今後やりたいこと

MVVM, Databindingの実装

## 各画面の役割
### メイン画面(MainActibity)
![image](https://raw.github.com/wiki/kasaiS-2-S-2/Cheat_Roulette/images/main.jpg)
![image](https://raw.github.com/wiki/kasaiS-2-S-2/Cheat_Roulette/images/nav_drawer.jpg)

セットされたルーレットを実際に使う画面です。

右下のFABを押すと、ルーレット作成画面、ルーレット編集画面、Myルーレット画面の3つの画面へ飛ぶことのできる複数のFABが現れます。
また、右上のハンバーガーボタンを押すとNavigation Drawerが開き、そこでは設定の変更やテーマの変更等を行うことができます。

### ルーレット作成画面(RouletteCreateActivity)
![image](https://raw.github.com/wiki/kasaiS-2-S-2/Cheat_Roulette/images/roulette_create.jpg)
![image](https://raw.github.com/wiki/kasaiS-2-S-2/Cheat_Roulette/images/cheat.jpg)

ルーレットを作成する画面です。

表示はRecyclerViewで行っています。

追加ボタンでルーレット項目の追加、右下のFABでルーレットを作成を完了します。作成したルーレットはRoomに保存が可能です。
ルーレット項目には、色、項目名、面積比を設定することができます。また、画面左上にある隠しボタンをタップすると、イカサマ設定が現れます。

イカサマ設定は以下の2種類で、

・必中スイッチ：ONにすると設定した項目が必ず当たるようになります。

・絶対ハズレスイッチ：ONにすると設定した項目は必ず外れます。

これらを駆使することでイカサマを行います。

### ルーレット編集画面(EditRouletteActivity)
![image](https://raw.github.com/wiki/kasaiS-2-S-2/Cheat_Roulette/images/edit_roulette.jpg)

メイン画面にセットされているルーレットを編集する画面です。

機能はルーレット作成画面と同じです。右下のFABを押すと、編集されたルーレットがメイン画面にセットされます。

### Myルーレット画面(MyRouletteActivity)
![image](https://raw.github.com/wiki/kasaiS-2-S-2/Cheat_Roulette/images/myRoulette.jpg)

保存されたルーレットの一覧を表示する画面です。

表示はRecyclerViewで行っています。

各CardViewには削除ボタンを押すと、そこに写っているルーレットがRoomから削除されます。編集ボタンを押すとMyルーレット編集画面へ遷移します。
また、CardView自体を押すと、そのルーレットがメイン画面にセットされます

### Myルーレット編集画面（EditMyRouletteActivity）

Myルーレットにて選択されたルーレットを編集する画面です。画面レイアウトはルーレット編集画面と同じです。

機能はルーレット作成画面と同じです。右下のFABを押すと、編集されたルーレットがRoomに上書きされます。

### 詳細設定画面（DetailSettingsActivity）
![image](https://raw.github.com/wiki/kasaiS-2-S-2/Cheat_Roulette/images/detail_setting.jpg)

アプリ全体の詳細な設定を行う画面です。

ここでPreferenceFragmentを表示させています。イカサマ設定がバレないための設定や、アラートダイアログを出すか出さないかを設定することができます。
