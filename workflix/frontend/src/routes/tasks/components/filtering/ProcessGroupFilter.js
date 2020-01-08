// @flow

import React from 'react'
import type { FiltersType } from '../../../../modules/datatypes/Filters'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import ProcessGroupMultiSelect from '../../../../modules/common/components/ProcessGroupMultiselect'

type PropsType = {|
  filters: FiltersType,
  onFiltersChanged: (FiltersType) => void,
  processGroups: Map<number, ProcessGroupType>
|}

class ProcessGroupFilter extends React.Component<PropsType> {
  onGroupsUpdated = (processGroups: ProcessGroupType[]) =>
    this.props.onFiltersChanged({
      ...this.props.filters,
      processGroups
    })

  render () {
    return <ProcessGroupMultiSelect
      allGroups={this.props.processGroups}
      placeholder='Filter process group…'
      selectedGroups={this.props.filters.processGroups || []}
      onSelectionChanged={this.onGroupsUpdated}
    />
  }
}

export default ProcessGroupFilter
