// @flow

import React from 'react'
import { Button, MenuItem, Tooltip } from '@blueprintjs/core'
import type { TaskTemplateType } from '../../../modules/datatypes/Task'
import type { ItemPredicate } from '@blueprintjs/select'
import { ItemRenderer, MultiSelect } from '@blueprintjs/select'
import highlightText from '../../../modules/common/highlightText'
import { difference } from 'lodash'

type PropsType = {
  task: TaskTemplateType,
  onChange: (task: TaskTemplateType) => void,
  allTasks: TaskTemplateType[],
  succs: TaskTemplateType[],
  possibleSuccs: TaskTemplateType[]
}

const TemplateSelect = MultiSelect.ofType<TaskTemplateType>()

const filterTaskTemplates: ItemPredicate<TaskTemplateType> = (query, task, _index, exactMatch) => {
  const normalizedName = task.name.toLocaleLowerCase()
  const normalizedQuery = query.toLocaleLowerCase()
  return exactMatch ? normalizedName === normalizedQuery : normalizedName.indexOf(normalizedQuery) >= 0
}

const tagRenderer = (task: TaskTemplateType) => task.name

class SuccessorSelect extends React.Component<PropsType> {
  renderTaskTemplate: ItemRenderer<TaskTemplateType> = (task, { handleClick, modifiers, query }) => {
    if (!modifiers.matchesPredicate) {
      return null
    }
    const menuItem = <MenuItem
      active={false}
      disabled={modifiers.disabled}
      icon={
        modifiers.disabled ? 'disable'
          : task.predecessors.indexOf(this.props.task.id) >= 0 ? 'tick' : 'blank'
      }
      key={task.id}
      onClick={handleClick}
      shouldDismissPopover={false}
      text={highlightText(task.name, query)}/>
    return modifiers.disabled ? <div><Tooltip content='Adding this would create a cyclic dependency.'>
        {menuItem}
      </Tooltip></div>
      : menuItem
  }

  handleTagRemove = (_tag: string, index: number) => {
    const { task, onChange, succs } = this.props
    const successor = succs[index]
    onChange({
      ...successor,
      predecessors: difference(successor.predecessors, [task.id])
    })
  }

  handleClearSuccs = () => {
    const { succs, onChange, task } = this.props
    succs.forEach(successor => onChange({
      ...successor,
      predecessors: difference(successor.predecessors, [task.id])
    }))
  }

  itemDisabled = (item: TaskTemplateType) => this.props.possibleSuccs.indexOf(item) < 0

  render () {
    const { task, allTasks, succs } = this.props

    const clearButton = task.predecessors.length > 0 &&
      <Button icon='key-backspace' minimal onClick={this.handleClearSuccs}/>

    return <TemplateSelect
      items={allTasks}
      itemPredicate={filterTaskTemplates}
      itemRenderer={this.renderTaskTemplate}
      tagRenderer={tagRenderer}
      itemDisabled={this.itemDisabled}
      tagInputProps={{
        onRemove: this.handleTagRemove,
        rightElement: clearButton
      }}
      fill
      selectedItems={succs}
      onItemSelect={this.handleSuccTaskSelect}
      popoverProps={{ usePortal: false }}
    />
  }

  handleSuccTaskSelect = (selectedTask: TaskTemplateType) => {
    const { task, onChange } = this.props
    if (selectedTask.predecessors.indexOf(task.id) < 0) {
      onChange({
        ...selectedTask,
        predecessors: [...selectedTask.predecessors, task.id]
      })
    } else {
      onChange({
        ...selectedTask,
        predecessors: difference(selectedTask.predecessors, [task.id])
      })
    }
  }
}

export default SuccessorSelect
