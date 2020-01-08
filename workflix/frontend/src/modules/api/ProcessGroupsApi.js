// @flow

import { safeFetch } from './SafeFetch'
import type { NewIdResultType } from './common'
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

  addMembership (groupId: number, memberId: string): Promise<NewIdResultType> {
    return safeFetch(`${processGroupsBackend}/${groupId}/members/${memberId}`, {
      method: 'PUT'
    })
      .then(result => result.json())
  }

  removeMembership (groupId: number, memberId: string): Promise<Response> {
    return safeFetch(`${processGroupsBackend}/${groupId}/members/${memberId}`, {
      method: 'DELETE'
    })
  }
}

export default ProcessGroupsApi
