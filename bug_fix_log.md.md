# Typing Speed Test — Bug Fix Log

**Student:** Rafiza Mimma
**Student ID:** 11240321848
**Course:** CSE 2216 — Software Development I

## Overview

During development and testing across Weeks 1-10, the following bugs were
identified and resolved. Each entry describes the issue, root cause, fix,
and verification.

**Total bugs logged:** 15
**Total bugs fixed:** 15
**Open bugs:** 0

---

## Bug #1 — NullPointerException on game start

**Reported:** Clicking any difficulty button caused the app to crash with
`java.lang.NullPointerException: Cannot invoke "JLabel.setText()"`.

**Root Cause:** The `statusIconLabel` was declared as a class field but
never instantiated inside `createGamePanel()`. When `startGame()` attempted
to call `statusIconLabel.setText(...)`, the field was still `null`.

**Fix:** Added instantiation in `createGamePanel()`:
```java
statusIconLabel = new JLabel("Ready", SwingConstants.CENTER);
headerRow.add(statusIconLabel, BorderLayout.CENTER);