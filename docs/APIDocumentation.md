# DumpsterController API Documentation

This API documentation covers the endpoints exposed by the DumpsterController in the BrainDumpstr application.

## Base URL

The base URL for all API endpoints is `/api/dumpstr`.

## Endpoints

### Add Tags to Dump

- **Method**: `PUT`
- **URL**: `/api/dumpstr/{dumpId}/tags/{tags}`
- **Path Variables**:
    - `dumpId`: (Long) The ID of the dump to add tags to.
    - `tags`: (Set<String>) A set of tags to add to the dump.
- **Response**: 204 No Content

### Remove Tags from Dump

- **Method**: `DELETE`
- **URL**: `/api/dumpstr/{dumpId}/tags/{tags}`
- **Path Variables**:
    - `dumpId`: (Long) The ID of the dump to remove tags from.
    - `tags`: (Set<String>) A set of tags to remove from the dump.
- **Response**: 204 No Content

### Create New Dump

- **Method**: `POST`
- **URL**: `/api/dumpstr/dump`
- **Request Parameters**:
    - `file`: (MultipartFile) The audio file for the new dump.
    - `context`: (String) The context for the new dump.
- **Response**: 200 OK
    - (Long) The ID of the created dump.

### Re-summarize Dump

- **Method**: `POST`
- **URL**: `/api/dumpstr/{dumpId}/collect`
- **Path Variables**:
    - `dumpId`: (Long) The ID of the dump to re-summarize.
- **Request Parameters**:
    - `file`: (MultipartFile) The new audio file for re-summarizing the dump.
    - `context`: (String) The new context for re-summarizing the dump.
- **Response**: 204 No Content

### Search Dumpster

- **Method**: `POST`
- **URL**: `/api/dumpstr/search`
- **Request Parameters**:
    - `StartDate`: (Optional<Date>) The start date of the search range.
    - `EndDate`: (Optional<Date>) The end date of the search range.
    - `Tags`: (Optional<Set<String>>) A set of tags to filter the search results.
    - `SearchText`: (Optional<String>) A text query to search in the dump content.
- **Response**: 200 OK
    - (Set<Long>) A set of dump IDs matching the search criteria.
