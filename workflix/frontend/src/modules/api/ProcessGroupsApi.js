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

  addProcessGroup (processGroup: ({ title: string, description: string, ownerId: string })): Promise<NewIdResultType> {
    return safeFetch(processGroupsBackend, {
      method: 'POST',
      body: JSON.stringify(processGroup)
    })
      .then(response => response.json())
  }

  patchProcessGroup (processGroup: ProcessGroupType): Promise<Response> {
    const { id, title, description, ownerId } = processGroup
    return safeFetch(`${processGroupsBackend}/${id}`, {
      method: 'PATCH',
      body: JSON.stringify({ title, description, ownerId })
    })
  }

  deleteProcessGroup (processGroupId: number): Promise<Response> {
    return safeFetch(`${processGroupsBackend}/${processGroupId}`, {
      method: 'DELETE'
    })
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
}

export default ProcessGroupsApi
