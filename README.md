# NoSpiderCleanerMod (Forge 1.20.1)

このMODは以下を実現します。

- クモ (`minecraft:spider`) と 洞窟グモ (`minecraft:cave_spider`) のスポーンを無効化
- 読み込まれたチャンク内の **蜘蛛の巣 (`minecraft:cobweb`)** を削除
- 読み込まれたチャンク内の **スポナー (`minecraft:spawner`)** を削除
- 新たに設置された蜘蛛の巣/スポナーも即時削除

## ビルド

```bash
./gradlew build
```

## 開発環境

- Minecraft 1.20.1
- Forge 47.x
- Java 17
