# Math Trail

An Android math practice app for students in grades 3-9. The first release focuses on multiplication and division, local child profiles, explicit save/resume, retry feedback, and correct-answer streaks.

## Current milestone

**Milestone 2 - Student profiles** (`feature/student-profiles`)

This milestone adds local student profile creation and switching. Profiles have a display name and avatar, survive app restarts, and provide an active student identity for later profile-scoped practice progress.

## Open in Android Studio

1. Open this folder in Android Studio.
2. Allow Gradle sync and install Android SDK Platform 36 if prompted.
3. Select an emulator or connected Android device and run the `app` configuration.

The project uses Gradle 8.13, Android Gradle Plugin 8.13.2, Kotlin 2.3.0, Java 17, and Jetpack Compose. The first local Gradle sync downloads dependencies.

## Project organization

The project separates UI rendering, product rules, and data access:

- `ui/`: Compose screens and screen state presentation
- `domain/`: profile validation and future question, retry, and streak rules
- `data/`: repository interfaces and local storage implementations

Student profile rules are independent of Compose, and storage is behind a repository interface so behavior can be tested without coupling product rules to Android preferences.

## Planned milestones

Each milestone or feature gets its own branch and focused commit(s) with explanatory commit messages.

1. **Project foundation and home screen** - complete; Android/Compose build, theme, grade selector, operation cards, and resume placeholder.
2. **Student profiles** - current; create/select local profiles with display name and avatar; active profile is saved locally.
3. **Practice engine** - grade/operation question rules, question UI, answer checking, three retries after the initial attempt, answer reveal, and explicit Next.
4. **Local progress and save/resume** - persistence per profile, saved checkpoints, resume behavior, and progress display.
5. **Streaks and finish polish** - current/best correct streak, accessibility, empty/error states, and final UI polish.
6. **Release readiness** - broader test coverage, build configuration, and handoff notes.

## Branch and PR workflow

- Keep `main` as the integration branch.
- Start each milestone or feature branch from the latest `main`, using `feature/<short-name>`.
- Make focused commits with descriptive subjects and bodies explaining the changes.
- When the feature is ready, create a pull request from its feature branch to `main`, then pause for Douglas to review and approve it.
- After approval, merge the PR, delete the merged feature branch on GitHub, update local `main` from `origin`, and create the next feature branch from that updated `main`.
- Do not begin the next feature before the approved PR has been merged and branch cleanup is complete.

## Product assumptions currently represented in the plan

- A child profile is identified by a nickname and avatar; no account or email is required in the local-only release.
- Students choose Multiplication or Division for a session.
- “Three retries” means four attempts total.
- The student advances only after pressing **Next**. **Save** stores an explicit checkpoint for later resume.
- The correct-in-a-row streak counts first-try successes; an incorrect attempt resets it. This rule can be adjusted during product review.
- Grade-to-skill mapping is configurable and should be aligned to the curriculum selected for the release.
