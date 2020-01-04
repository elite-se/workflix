// @flow

import { safeFetch } from './SafeFetch'
import { BACKEND } from './common'
import type { ProcessGroupType } from '../datatypes/ProcessGroup'

const processGroupsBackend = `${BACKEND}/processGroups`

class ProcessGroupsApi {
  getProcessGroups (): Promise<Map<number, ProcessGroupType>> {
    return safeFetch(processGroupsBackend)
      .then(response => response.json())
      .then(json => json.groups)
      .then(groups => groups.map(group => ({
        // convert ISO string to Date
        ...group,
        createdAt: group.createdAt && new Date(group.createdAt)
      })))
      .then(procGroups => new Map(procGroups.map(group => [group.id, group])))
  }
}

export default ProcessGroupsApi
