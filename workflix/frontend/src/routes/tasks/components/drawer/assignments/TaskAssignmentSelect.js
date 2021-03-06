// @flow

import React from 'react'
import { IItemRendererProps, ItemRenderer, MultiSelect } from '@blueprintjs/select'
import type { UserType } from '../../../../../modules/datatypes/User'
import type { TaskAssignmentType, TaskType } from '../../../../../modules/datatypes/Task'
import { Button, MenuItem } from '@blueprintjs/core'
import ProcessApi from '../../../../../modules/api/ProcessApi'
import AssigneeTagContent from './AssigneeTagContent'
import { sortBy } from 'lodash'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'
import { toastifyError } from '../../../../../modules/common/toastifyError'

const UserSelect = MultiSelect.ofType<UserType>()

type PropsType = {
  task: TaskType,
  onAssignmentsChanged: (TaskAssignmentType[]) => void,
  users: Map<string, UserType>,
  isReadOnly: boolean
}

class TaskAssignmentSelect extends React.Component<PropsType> {
  usersArray: UserType[] = Array.from(this.props.users.values())

  renderUser (user: UserType, { handleClick, modifiers }: IItemRendererProps): ItemRenderer<UserType> {
    if (!modifiers.matchesPredicate) {
      return null
    }
    return <MenuItem active={modifiers.active}
                     key={user.id}
                     label={user.displayname}
                     onClick={handleClick}
                     text={user.name}
                     shouldDismissPopover={false}/>
  }

  renderUserToTag = (user: UserType) => <AssigneeTagContent assignee={user}/>

  onItemSelect = (item: UserType) => {
    const task = this.props.task
    if (task.assignments.find(ass => ass.assigneeId === item.id)) {
      return
    }
    new ProcessApi().addAssignee(task.id, item.id)
      .then(json => {
        this.props.onAssignmentsChanged(task.assignments.concat({
          id: json.newId,
          assigneeId: item.id,
          closed: false,
          createdAt: undefined,
          doneAt: undefined
        }))
      })
      .catch(toastifyError)
  }

  onClear = () => {
    const task = this.props.task
    const api = new ProcessApi()
    Promise.all(task.assignments.map(ass => api.removeAssignee(task.id, ass.assigneeId)))
      .then(() => {
        this.props.onAssignmentsChanged([])
      })
      .catch(toastifyError)
  }

  onTagRemoved = (tag: AssigneeTagContent) => {
    const task = this.props.task
    const removedAssignee = tag.props.assignee
    new ProcessApi().removeAssignee(task.id, removedAssignee.id)
      .then(() => {
        this.props.onAssignmentsChanged(task.assignments.filter(ass => ass.assigneeId !== removedAssignee.id))
      })
      .catch(toastifyError)
  }

  itemPredicate = (query: string, item: UserType, index?: number, exactMatch?: boolean) => {
    if (this.props.task.assignments.find(ass => ass.assigneeId === item.id)) {
      return false
    }
    if (exactMatch) {
      return item.name.toLowerCase() === query.toLowerCase()
    } else {
      return item.name.toLowerCase().includes(query.toLowerCase())
    }
  }

  /** filter for users with names that contain the query and which are not yet assigned
   show names that begin with the query first, then sort alphabetically **/
  itemListPredicate = (query: string, items: UserType[]) => {
    const qLower = query.toLocaleLowerCase()
    // noinspection UnnecessaryLocalVariableJS (flow will fail otherwise)
    const filteredUsers: UserType[] = items.filter(item =>
      item.name.toLocaleLowerCase().includes(qLower) &&
      !this.props.task.assignments.find(ass => ass.assigneeId === item.id)
    )
    const filteredAndSorted: UserType[] = sortBy(
      filteredUsers,
      item => !item.name.toLocaleLowerCase().startsWith(qLower),
      item => item.name.toLocaleLowerCase()
    )
    return filteredAndSorted
  }

  tagProps = (tag: AssigneeTagContent) => {
    const assignment = this.props.task.assignments.find(ass => ass.assigneeId === tag.props.assignee.id)
    const done = assignment && assignment.closed
    return done ? {
      intent: Intent.SUCCESS,
      icon: 'tick'
    } : {
      intent: Intent.PRIMARY
    }
  }

  render () {
    const { task, isReadOnly } = this.props
    const assignees = this.usersArray
      .filter(user => task.assignments.find(ass => ass.assigneeId === user.id))
    const clearButton = task.assignments.length > 0 && !isReadOnly &&
      <Button icon='cross' minimal onClick={this.onClear}/>
    return <UserSelect
      items={this.usersArray}
      itemRenderer={this.renderUser}
      onItemSelect={this.onItemSelect}
      tagRenderer={this.renderUserToTag}
      fill
      popoverProps={{ usePortal: false }}
      selectedItems={assignees}
      tagInputProps={{
        onRemove: this.onTagRemoved,
        rightElement: clearButton,
        tagProps: this.tagProps,
        disabled: isReadOnly
      }}
      itemPredicate={this.itemPredicate}
      itemListPredicate={this.itemListPredicate}
      resetOnSelect/>
  }
}

export default TaskAssignmentSelect
