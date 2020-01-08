// @flow

import React from 'react'
import { ItemListPredicate, ItemRenderer, MultiSelect } from '@blueprintjs/select'
import { MenuItem } from '@blueprintjs/core'
import { filter, sortBy, uniq, without } from 'lodash'
import type { ProcessGroupType } from '../../datatypes/ProcessGroup'
import highlightText from '../highlightText'

const BlueprintProcessGroupMultiSelect = MultiSelect.ofType<ProcessGroupType>()

type PropsType = {|
  allGroups: Map<number, ProcessGroupType>,
  selectedGroups: ProcessGroupType[],
  onSelectionChanged: (processGroups: ProcessGroupType[]) => void,
  placeholder?: string,
  autoWidth?: boolean
|}

class ProcessGroupMultiSelect extends React.Component<PropsType> {
  renderProcessGroupTag (group: ProcessGroupType): React$Node {
    return group.title
  }

  renderProcessGroup: ItemRenderer<ProcessGroupType> = (group, { modifiers, handleClick, query }) => {
    if (!modifiers.matchesPredicate) {
      return null
    }
    return <MenuItem
      active={modifiers.active}
      icon={this.isSelected(group) ? 'tick' : 'blank'}
      key={group.id}
      onClick={handleClick}
      text={highlightText(group.title, query)}
      shouldDismissPopover={false}
    />
  }

  isSelected = (group: ProcessGroupType) => this.props.selectedGroups.includes(group)

  onGroupSelect = (group: ProcessGroupType) =>
    this.props.onSelectionChanged(
      this.isSelected(group)
        ? without(this.props.selectedGroups, group)
        : uniq([...this.props.selectedGroups, group]))

  onTagRemove = (tag: string, index: number) => {
    this.props.onSelectionChanged(this.props.selectedGroups.filter((_, idx) => idx !== index))
  }

  itemListPredicate: ItemListPredicate<ProcessGroupType> = (query, items) => {
    const queryLower = query.toLocaleLowerCase()
    return sortBy(filter(items, group => group.title.toLocaleLowerCase().includes(queryLower)),
      group => !group.title.toLocaleLowerCase().startsWith(queryLower),
      group => group.title.toLocaleLowerCase()
    )
  }

  render () {
    return <BlueprintProcessGroupMultiSelect
      itemListPredicate={this.itemListPredicate}
      itemRenderer={this.renderProcessGroup}
      items={Array.from(this.props.allGroups.values())}
      noResults={<MenuItem icon='disable' text='No results.' disabled/>}
      onItemSelect={this.onGroupSelect}
      tagRenderer={this.renderProcessGroupTag}
      placeholder={this.props.placeholder}
      tagInputProps={{
        onRemove: this.onTagRemove,
        inputProps: this.props.autoWidth && { style: { width: 'auto' } }
      }}
      selectedItems={this.props.selectedGroups}
    />
  }
}

export default ProcessGroupMultiSelect
