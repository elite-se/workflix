// @flow

import { safeFetch } from './SafeFetch'
import { BACKEND } from './common'
import type { ProcessGroupType } from '../datatypes/ProcessGroup'
import { parseDatesInProcessGroup } from './parseDates'

const processGroupsBackend = `${BACKEND}/processGroups`

class ProcessGroupsApi {
  getProcessGroups (): Promise<Map<number, ProcessGroupType>> {
    return safeFetch(processGroupsBackend)
      .then(response => response.json())
      .then(json => json.groups)
      .then(groups => groups.map(parseDatesInProcessGroup))
      .then(procGroups => new Map(procGroups.map(group => [group.id, group])))
  }
}

export default ProcessGroupsApi
