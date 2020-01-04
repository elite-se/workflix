// @flow

import React from 'react'
import type { FiltersType } from '../../../../modules/datatypes/Filters'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import { ItemListPredicate, ItemRenderer, MultiSelect } from '@blueprintjs/select'
import { ProcessGroupRenderItem } from './ProcessGroupRenderItem'
import { MenuItem } from '@blueprintjs/core'
import { filter, sortBy, uniq, without } from 'lodash'
import highlightText from '../../../../modules/common/highlightText'

const ProcessGroupMultiSelect = MultiSelect.ofType<ProcessGroupType>()

type PropsType = {|
  filters: FiltersType,
  onFiltersChanged: (FiltersType) => void,
  processGroups: Map<number, ProcessGroupType>
|}

class ProcessGroupFilter extends React.Component<PropsType> {
  renderProcessGroupTag (group: ProcessGroupType): React$Node {
    return <ProcessGroupRenderItem procGroup={group}/>
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

  getSelectedGroups = () => this.props.filters.processGroups || []
  isSelected = (group: ProcessGroupType) => this.getSelectedGroups().includes(group)

  onGroupSelect = (group: ProcessGroupType) =>
    this.onGroupsUpdated(
      this.isSelected(group)
        ? without(this.getSelectedGroups(), group)
        : uniq([...this.getSelectedGroups(), group]))

  onTagRemove = (tag: ProcessGroupRenderItem) => {
    this.onGroupsUpdated(without(this.getSelectedGroups(), tag.props.procGroup))
  }

  onGroupsUpdated = (processGroups: ProcessGroupType[]) =>
    this.props.onFiltersChanged({
      ...this.props.filters,
      processGroups
    })

  itemListPredicate: ItemListPredicate<ProcessGroupType> = (query, items) => {
    const queryLower = query.toLocaleLowerCase()
    return sortBy(filter(items, group =>
      group.title.toLocaleLowerCase().includes(queryLower)),
    group => !group.title.toLocaleLowerCase().startsWith(queryLower),
    group => group.title.toLocaleLowerCase()
    )
  }

  render () {
    return <ProcessGroupMultiSelect
      itemListPredicate={this.itemListPredicate}
      itemRenderer={this.renderProcessGroup}
      items={Array.from(this.props.processGroups.values())}
      noResults={<MenuItem icon='disable' text='No results.' disabled/>}
      onItemSelect={this.onGroupSelect}
      tagRenderer={this.renderProcessGroupTag}
      placeholder='Filter process group…'
      tagInputProps={{
        onRemove: this.onTagRemove,
        inputProps: { style: { width: 'auto' } }
      }}
      selectedItems={this.props.filters.processGroups}
    />
  }
}

export default ProcessGroupFilter
