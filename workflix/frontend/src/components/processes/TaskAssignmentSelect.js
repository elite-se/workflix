// @flow

import React from 'react'
import { IItemRendererProps, ItemRenderer, MultiSelect } from '@blueprintjs/select'
import type { UserType } from '../../datatypes/models'
import type { TaskAssignmentType, TaskType } from '../../datatypes/TaskType'
import { Button, MenuItem } from '@blueprintjs/core'
import ProcessApi from '../../api/ProcessApi'

const UserSelect = MultiSelect.ofType<UserType>()

type PropsType = {
  task: TaskType,
  onTaskModified: (TaskType) => void,
  users: Map<string, UserType>
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
                     shouldDismissPopover={false} />
  }

  renderUserToTag (user: UserType): string {
    return user.name
  }

  onItemSelect = (item: UserType) => {
    const task = this.props.task
    if (task.assignments.map(ass => ass.assigneeId).find(id => id === item.id)) { return }
    this.setAssignments(task.assignments.concat({
      id: undefined,
      assigneeId: item.id,
      status: 'TODO',
      createdAt: 'not yet implemented',
      doneAt: undefined
    }))
  }

  onClear = () => {
    this.setAssignments([])
  }

  onTagRemoved = (tag: string, index: number) => {
    const task = this.props.task
    this.setAssignments(task.assignments.filter((t, tidx) => tidx !== index))
  }

  setAssignments = (assignments: TaskAssignmentType[]) => {
    new ProcessApi().patchAssignments({
      ...(this.props.task),
      assignments: assignments
    })
      .then(() => {
        this.props.task.assignments = assignments
        this.props.onTaskModified(this.props.task)
      })
      .catch(err => console.error(err))
  }

  itemPredicate = (query: string, item: UserType, index?: number, exactMatch?: boolean) => {
    if (this.props.task.assignments.find(ass => ass.assigneeId === item.id)) { return false }
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
    const filterdAndSorted: UserType[] = items.filter(item =>
      item.name.toLocaleLowerCase().includes(qLower) &&
      !this.props.task.assignments.find(ass => ass.assigneeId === item.id)
    ).sort((a, b) => {
      const aLower = a.name.toLocaleLowerCase()
      const bLower = b.name.toLocaleLowerCase()
      const aStart = aLower.startsWith(qLower)
      const bStart = bLower.startsWith(qLower)
      if (aStart && !bStart) {
        return -1
      } else if (!aStart && bStart) {
        return 1
      } else {
        return aLower.localeCompare(bLower)
      }
    })
    return filterdAndSorted
  }

  render () {
    const task = this.props.task
    const assignees = this.usersArray
      .filter(user => task.assignments.map(assignees => assignees.assigneeId)
        .find(assId => assId === user.id))
    const clearButton =
      task.assignments.length > 0 ? <Button icon='cross' minimal onClick={this.onClear} /> : undefined
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
        rightElement: clearButton
      }}
      itemPredicate={this.itemPredicate}
      itemListPredicate={this.itemListPredicate}
      resetOnSelect />
  }
}
export default TaskAssignmentSelect
