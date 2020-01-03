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
  possiblePreds: TaskTemplateType[]
}

const TemplateSelect = MultiSelect.ofType<TaskTemplateType>()

const filterTaskTemplates: ItemPredicate<TaskTemplateType> = (query, task, _index, exactMatch) => {
  const normalizedName = task.name.toLocaleLowerCase()
  const normalizedQuery = query.toLocaleLowerCase()
  return exactMatch ? normalizedName === normalizedQuery : normalizedName.indexOf(normalizedQuery) >= 0
}

const tagRenderer = (task: TaskTemplateType) => task.name

class PredecessorSelect extends React.Component<PropsType> {
  renderTaskTemplate: ItemRenderer<TaskTemplateType> = (task, { handleClick, modifiers, query }) => {
    if (!modifiers.matchesPredicate) {
      return null
    }
    const menuItem = <MenuItem
      active={false}
      disabled={modifiers.disabled}
      icon={
        modifiers.disabled ? 'disable'
          : this.props.task.predecessors.indexOf(task.id) >= 0 ? 'tick' : 'blank'
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
    const { task, onChange } = this.props
    onChange({
      ...task,
      predecessors: difference(task.predecessors, [task.predecessors[index]])
    })
  }

  handleClearPreds = () => {
    this.props.onChange({
      ...this.props.task,
      predecessors: []
    })
  }

  itemDisabled = (item: TaskTemplateType) => this.props.possiblePreds.indexOf(item) < 0

  render () {
    const { task, allTasks } = this.props

    const clearButton = task.predecessors.length > 0 &&
      <Button icon='key-backspace' minimal onClick={this.handleClearPreds}/>

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
      selectedItems={task.predecessors.map(id => allTasks.find(_task => _task.id === id))}
      onItemSelect={this.handlePredTaskSelect}
    />
  }

  handlePredTaskSelect = (selectedTask: TaskTemplateType) => {
    const { task, onChange } = this.props
    const index = task.predecessors.indexOf(selectedTask.id)
    if (index < 0) {
      onChange({
        ...task,
        predecessors: [...task.predecessors, selectedTask.id]
      })
    } else {
      onChange({
        ...task,
        predecessors: difference(task.predecessors, [selectedTask.id])
      })
    }
  }
}

export default PredecessorSelect
