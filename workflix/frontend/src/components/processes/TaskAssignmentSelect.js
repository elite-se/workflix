// @flow

import React from 'react'
import { IItemRendererProps, ItemRenderer, MultiSelect } from '@blueprintjs/select'
import type { UserType } from '../../datatypes/models'
import type { TaskType } from '../../datatypes/TaskType'
import { Button, MenuItem } from '@blueprintjs/core'

const UserSelect = MultiSelect.ofType<UserType>()

type PropsType = {
  task: TaskType
}

const dummyUsers: UserType[] =
  [{
    displayname: 'CK',
    name: 'Christ Kindle',
    id: 'christkindle-seine-id',
    email: 'christkind@himmel.noncom'
  },
  {
    displayname: 'WM',
    name: 'Weihnachts Mann',
    id: 'weihnachtsmann-seine-id',
    email: 'weihnachtsmann@cocacola.com'
  },
  {
    displayname: 'SN',
    name: 'Sankt Nikolaus',
    id: 'nikolaus-seine-id',
    email: 'nikolaus@himmel.noncom'
  }
  ]

class TaskAssignmentSelect extends React.Component<PropsType> {
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
    return user.displayname
  }

  onItemSelect = (item: UserType) => {
    const task = this.props.task
    if (task.assignments.map(ass => ass.assigneeId).find(id => id === item.id)) { return }
    task.assignments = task.assignments.concat({
      assigneeId: item.id,
      status: 'TODO',
      createdAt: 'not yet implemented',
      doneAt: undefined
    })
  }

  onClear = () => { this.props.task.assignments = [] }

  onTagRemoved = (tag: string, index: number) => {
    const task = this.props.task
    task.assignments = task.assignments.slice(0, index - 1)
      .concat(task.assignments.slice(index, task.assignments.length))
  }

  render () {
    const task = this.props.task
    const assignees = dummyUsers
      .filter(user => task.assignments.map(assignees => assignees.assigneeId)
        .find(assId => assId === user.id))
    console.debug(assignees)
    const clearButton =
      task.assignments.length > 0 ? <Button icon='cross' minimal onClick={this.onClear()} /> : undefined
    return <UserSelect
      items={dummyUsers}
      itemRenderer={this.renderUser}
      onItemSelect={this.onItemSelect}
      tagRenderer={this.renderUserToTag}
      fill
      noResults={<MenuItem disabled text='No results.' />}
      popoverProps={{ usePortal: false }}
      selectedItems={assignees}
      tagInputProps={{
        onRemove: this.onTagRemoved,
        rightElement: clearButton
      }} />
  }
}
export default TaskAssignmentSelect
