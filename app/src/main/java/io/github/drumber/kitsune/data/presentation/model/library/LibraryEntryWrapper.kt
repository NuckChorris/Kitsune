package io.github.drumber.kitsune.data.presentation.model.library

data class LibraryEntryWrapper(
    val libraryEntry: LibraryEntry,
    val libraryModification: LibraryEntryModification?
) {

    val isSynchronizing
        get() = libraryModification?.state == LibraryModificationState.SYNCHRONIZING

    val progress
        get() = libraryModification?.progress ?: libraryEntry.progress

    val volumesOwned
        get() = libraryModification?.volumesOwned ?: libraryEntry.volumesOwned

    val ratingTwenty
        get() = libraryModification?.ratingTwenty ?: libraryEntry.ratingTwenty

    val status
        get() = libraryModification?.status ?: libraryEntry.status

    val reconsumeCount
        get() = libraryModification?.reconsumeCount ?: libraryEntry.reconsumeCount

    val isPrivate
        get() = libraryModification?.privateEntry ?: libraryEntry.privateEntry

    val startedAt
        get() = libraryModification?.startedAt ?: libraryEntry.startedAt

    val finishedAt
        get() = libraryModification?.finishedAt ?: libraryEntry.finishedAt

    val notes
        get() = libraryModification?.notes ?: libraryEntry.notes

    val isNotSynced
        get() = !isSynchronizing &&
                libraryModification != null &&
                !libraryModification.isEqualToLibraryEntry(libraryEntry)

}
