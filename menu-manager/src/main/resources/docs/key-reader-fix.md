# Fix for KeyReader

## Problem Description
The `KeyReader` was not handling certain keys correctly, leading to errors when processing keyboard events.

## Solution
- Added validation to ensure that only valid keys are processed.
- Improved the logic for reading and interpreting key events.

## Testing
- Unit tests have been added to verify that the `KeyReader` now correctly handles all key events.
- Integration tests have been run to ensure that the menu system works as expected with the updated `KeyReader`.