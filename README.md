# WLTPMorphPlugin

**WLTPMorphPlugin**은 플레이어가 원하는 **블럭**으로 변신할 수 있게 해주는 마인크래프트 Paper 서버용 플러그인입니다.  
외부 추가 플러그인 없이 작동하며, `BlockDisplay`와 `ArmorStand`를 활용해 자체 구현된 변신 시스템을 제공합니다.

**WLTPMorphPlugin** is a Minecraft Paper server plugin that allows players to morph into any **block** they want.  
It works without any external plugins and uses `BlockDisplay` and `ArmorStand` to implement a fully custom morphing system.

---

## ✅ 주요 기능

- 플레이어가 원하는 **블럭(Material)** 으로 변신 가능
- `ArmorStand + BlockDisplay` 조합으로 자연스러운 블럭 형태 구현
- 이동 시 자연스럽게 추적
- `/morph` 명령어로 블럭으로 변신
- `/unmorph` 명령어로 원래대로 복귀
- 완전한 투명 상태 적용 (`Player#setInvisible(true)`)

## ✅ Features

- Morph into any **block (Material)** of your choice
- Natural block form using `ArmorStand + BlockDisplay`
- Smooth tracking movement while morphed
- Use `/morph` command to morph into a block
- Use `/unmorph` command to revert back
- Fully invisible player state (`Player#setInvisible(true)`)

---

## 🛠️ 개발 환경

| 구성 요소       | 버전 / 정보                              |
|----------------|--------------------------------------|
| IDE            | IntelliJ IDEA                        |
| JDK            | Java 21                              |
| 빌드 시스템    | Gradle (Groovy DSL)                  |
| 서버 API       | Paper 1.21.7 (Tested)                |
| 사용 기술      | Bukkit API, BlockDisplay, ArmorStand |
| 외부 라이브러리 | 없음                                   |

`org.joml` 라이브러리 사용으로 `Transformation`, `AxisAngle4f`, `Vector3f` 등을 활용합니다.

---

## 🛠️ Development Environment

| Component       | Version / Info                     |
|-----------------|----------------------------------|
| IDE             | IntelliJ IDEA                    |
| JDK             | Java 21                         |
| Build System    | Gradle (Groovy DSL)             |
| Server API      | Paper 1.21.7 (Tested)            |
| Technologies    | Bukkit API, BlockDisplay, ArmorStand |
| External Libs   | None                            |

Uses `org.joml` library for `Transformation`, `AxisAngle4f`, `Vector3f`, etc.

---

## 🧩 설치 방법

1. [PaperMC](https://papermc.io/downloads) 1.21.7 이상 버전의 서버를 준비합니다.
2. `WLTPMorphPlugin.jar` 파일을 서버의 `plugins` 폴더에 넣습니다.
3. 서버를 재시작합니다.

---

## 🧩 Installation

1. Prepare a PaperMC 1.21.7+ server from [papermc.io](https://papermc.io/downloads).
2. Put `WLTPMorphPlugin.jar` into your server's `plugins` folder.
3. Restart your server.

---

## 🕹️ 명령어

| 기능          | 명령어                  | 예시                                                                  |
|---------------|-------------------------|---------------------------------------------------------------------|
| 블럭으로 변신 | `/morph <블럭이름>`     | `/morph stone` <br> `/morph GRASS_BLOCK`                            |
| 변신 해제     | `/unmorph`              | `/unmorph`                                                          |

---

## 🕹️ Commands

| Function        | Command                 | Example                                                        |
|-----------------|-------------------------|----------------------------------------------------------------|
| Morph into block| `/morph <block_name>`    | `/morph stone` <br> `/morph GRASS_BLOCK`                      |
| Revert morph    | `/unmorph`              | `/unmorph`                                                    |

---

## ⚙️ 권한 노드

| 권한 노드           | 설명                     |
|---------------------|--------------------------|
| `morph.use`         | `/morph` 명령어 사용 허용 |
| `morph.unmorph`     | `/unmorph` 명령어 사용 허용 |

기본적으로 OP만 명령어를 사용할 수 있습니다.

---

## ⚙️ Permissions

| Permission Node  | Description                        |
|------------------|----------------------------------|
| `morph.use`      | Allows using the `/morph` command |
| `morph.unmorph`  | Allows using the `/unmorph` command |

By default, only OPs can use these commands.

---

## 📌 주의 사항

- 변신된 블럭은 히트박스와 충돌 판정이 없습니다.
- 웅크리기(Sneak) 시 디스플레이 블럭이 함께 내려갈 수 있습니다.
- 서버에서 `BlockDisplay`, `ArmorStand` 생성이 제한되면 기능이 제대로 작동하지 않을 수 있습니다.

---

## 📌 Notes

- Morphed blocks have no hitbox or collision.
- When sneaking, the display block may visually shift downward.
- If the server restricts `BlockDisplay` or `ArmorStand` spawning, the plugin might not function correctly.

---

## 📄 라이선스

이 플러그인은 자유롭게 사용할 수 있으며, MIT 라이선스를 따릅니다.  
출처 표기는 필수입니다.

---

## 📄 License

This plugin is free to use under the MIT License.  
Please keep the attribution to the original author.

---

## 🙋 지원 및 기여

- 버그 제보, 피드백, 기여는 GitHub 이슈나 Pull Request를 통해 환영합니다.
- 문의 사항은 Discord(wltp_) 또는 이메일을 통해 전달해주세요.

---

## 🙋 Support & Contribution

- Bug reports, feedback, and contributions are welcome via GitHub issues or pull requests.
- For inquiries, contact via Discord (wltp_) or email.

---

# TODO List

## 기능 개선
- [ ] 몬스터 변신 기능 추가 및 테스트
- [ ] 웅크릴 때 위치 보정 최적화
- [ ] 명령어 자동완성(TabComplete) 추가

## Feature Improvements
- [ ] Add and test monster morph feature
- [ ] Optimize position correction when sneaking
- [ ] Add command tab-completion

## 버그 수정
- [ ] None

## Bug Fixes
- [ ] None

## 문서화
- [ ] 설치 및 사용법 동영상 제작
- [ ] GitHub 릴리즈 노트 작성

## Documentation
- [ ] Create installation and usage tutorial videos
- [ ] Write GitHub release notes

## 테스트
- [ ] 다양한 블럭 및 몬스터 변신 테스트
- [ ] 멀티플레이 환경 스트레스 테스트

## Testing
- [ ] Test various block and monster morphs
- [ ] Stress test in multiplayer environments

## 기타
- [ ] None

## Others
- [ ] None
