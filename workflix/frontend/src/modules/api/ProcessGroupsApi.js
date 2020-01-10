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

  addMembership (groupId: number, memberId: string): Promise<Response> {
    return safeFetch(`${processGroupsBackend}/${groupId}/members/${memberId}`, {
      method: 'PUT'
    })
  }

  removeMembership (groupId: number, memberId: string): Promise<Response> {
    return safeFetch(`${processGroupsBackend}/${groupId}/members/${memberId}`, {
      method: 'DELETE'
    })
  }

  patchProcessGroup (processGroup: ProcessGroupType): Promise<Response> {
    return safeFetch(`${processGroupsBackend}/${processGroup.id}`, {
      method: 'PATCH',
      body: JSON.stringify(processGroup)
    })
  }
}

export default ProcessGroupsApi
