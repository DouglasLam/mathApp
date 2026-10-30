# Math Trail

An Android math practice app for students in grades 3-9. The first release focuses on multiplication and division, local child profiles, explicit save/resume, retry feedback, and correct-answer streaks.

## Current milestone

**Milestone 1 - Project foundation and home screen** (`feature/project-foundation`)

This milestone establishes the Android/Compose build, app identity, initial home screen, and the branching/PR workflow. The home screen is a UI foundation: grade selection works locally, while profile management, navigation, practice, and persistence are planned for later milestones.

## Open in Android Studio

1. Open this folder in Android Studio.
2. Allow Gradle sync and install Android SDK Platform 36 if prompted.
3. Select an emulator or connected Android device and run the `app` configuration.

The project uses Gradle 8.13, Android Gradle Plugin 8.13.2, Kotlin 2.3.0, Java 17, and Jetpack Compose. The first local Gradle sync downloads dependencies.

## Project organization

The project will keep UI rendering, practice rules, and data access in separate layers as the features are added:

- `ui/`: Compose screens and screen state presentation
- `domain/`: grade rules, question generation, answer/retry behavior, streak calculations, and use cases
- `data/`: repository interfaces and local Room implementations

The foundation milestone currently keeps its small home-screen prototype in `MainActivity.kt`. Follow-on milestones should move screens into `ui/` and business rules into `domain/` before adding substantial behavior.

## Planned milestones

Each milestone or feature gets its own branch, a focused commit with an explanatory body, and a PR. Work pauses after the PR description is provided. Continue only after Douglas confirms the PR was merged.

1. **Project foundation and home screen** - current branch; Compose project, theme, grade selector, operation cards, and resume placeholder.
2. **Student profiles** - create/select local profiles with display name and avatar; profile-scoped state.
3. **Practice engine** - grade/operation question rules, question UI, answer checking, three retries after the initial attempt, answer reveal, and explicit Next.
4. **Local progress and save/resume** - persistence per profile, saved checkpoints, resume behavior, and progress display.
5. **Streaks and finish polish** - current/best correct streak, accessibility, empty/error states, and final UI polish.
6. **Release readiness** - broader test coverage, build configuration, and handoff notes.

## Branch and PR workflow

- Keep `main` as the integration branch.
- Start each next milestone from the updated `main` using a new branch named `feature/<short-name>`.
- Make a focused commit with a descriptive subject and a body that explains what changed and why.
- Stop after the commit and provide a PR title, description, and review notes.
- After PR merge confirmation, update local `main` from the hosted remote, then start the next branch.

This repository currently has no remote configured. Add the remote URL after creating the hosted repository, then push the feature branch to open the PR.

## Product assumptions currently represented in the plan

- A child profile is identified by a nickname and avatar; no account or email is required in the local-only release.
- Students choose Multiplication or Division for a session.
- “Three retries” means four attempts total.
- The student advances only after pressing **Next**. **Save** stores an explicit checkpoint for later resume.
- The correct-in-a-row streak counts first-try successes; an incorrect attempt resets it. This rule can be adjusted during product review.
- Grade-to-skill mapping is configurable and should be aligned to the curriculum selected for the release.
